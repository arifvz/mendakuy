package com.arif.mendakuy.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arif.mendakuy.App
import com.arif.mendakuy.JoinFragment
import com.arif.mendakuy.data.model.Post
import com.arif.mendakuy.databinding.FragmentHomeBinding
import com.arif.mendakuy.ui.adapter.PostAdapter
import com.arif.mendakuy.ui.chat.ChatActivity
import com.arif.mendakuy.ui.chat.ContentChatActivity
import com.arif.mendakuy.ui.login.LoginActivity

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val homeadapter = PostAdapter(arrayListOf())

    val dialogJoin = JoinFragment()

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            recycleView.adapter = homeadapter
            recycleView.layoutManager = LinearLayoutManager(context)

            swipeRefresh.setOnRefreshListener {
                homeViewModel.getPost()
            }

            btnMessage.setOnClickListener {
                val intent = Intent(view.context, ChatActivity::class.java)
                view.context.startActivity(intent)
            }
        }

        progressBar = ProgressBar(context)
        homeViewModel.getPost()
        homeViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeViewState.Progress -> onProgress(state.isLoading)
                is HomeViewState.ShowPost -> onShowPost(state.postlist)
                is HomeViewState.JoinResult -> onJoinResult(state.post, state.message)
            }
        }

        dialogJoin.callback = object : JoinFragment.Callback {
            override fun onSubmit(message: String) {
                homeViewModel.onJoin(dialogJoin.getPost(), message)
                dialogJoin.dismiss()
            }
        }

        homeadapter.callback = object : PostAdapter.Callback {
            override fun onClickJoin(post: Post) {
                if (App.isLogin()) {
                    dialogJoin.setPost(post)
                    dialogJoin.show(childFragmentManager, "dialog")
                } else {
                    val intent = Intent(context, LoginActivity::class.java)
                    activity?.startActivity(intent)
                }
            }
        }
    }

    private fun onJoinResult(post: Post?, message: String?) {
        Toast.makeText(context, "THE JOIN REQUEST WAS SUCCESSFULL ", Toast.LENGTH_SHORT).show()
        dialogJoin.dismiss()
    }

    private fun onShowPost(postlist: List<Post>) {
        homeadapter.setData(postlist)
    }

    private fun onProgress(loading: Boolean) {
        if (loading) {
            progressBar.isVisible
        } else {
            progressBar.isInvisible
        }
        binding.swipeRefresh.isRefreshing = loading
    }
}
