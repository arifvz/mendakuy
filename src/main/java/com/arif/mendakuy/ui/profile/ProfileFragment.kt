package com.arif.mendakuy.ui.profile

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import com.arif.mendakuy.App.Companion.getAvatarUrl
import com.arif.mendakuy.MainActivity
import com.arif.mendakuy.R
import com.arif.mendakuy.data.AuthManager
import com.arif.mendakuy.data.model.Post
import com.arif.mendakuy.databinding.FragmentProfileBinding
import com.arif.mendakuy.ui.adapter.PostAdapter
import com.arif.mendakuy.ui.updateprofile.UpdateProfileActivity
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding : FragmentProfileBinding? = null

    private lateinit var progressDialog: ProgressDialog

    private val binding get() = _binding!!

    private val followingAdapter = PostAdapter(arrayListOf())
    private val createdAdapter = PostAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdate.setOnClickListener{
            val intent = Intent(context, UpdateProfileActivity::class.java)
            startActivity(intent)
        }

        progressDialog = ProgressDialog(context)

        binding.activityView.adapter = followingAdapter
        binding.activityView.layoutManager = LinearLayoutManager(context)
        binding.activityView.adapter = createdAdapter
        binding.activityView.layoutManager = LinearLayoutManager(context)

        profileViewModel.getFollowingPost()
        profileViewModel.getCreatedPost()

        followingAdapter.isShowJoin = false
        createdAdapter.isShowJoin = false

        binding.btnLogOut.setOnClickListener {
            val ctx = context ?: return@setOnClickListener
            AuthManager.logOut(ctx)

            val intent = Intent(ctx, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.btnFollow.setOnClickListener {
            binding.activityView.adapter = followingAdapter
            binding.btnCreated.setBackgroundResource(R.color.grey)
            binding.btnFollow.setBackgroundResource(R.color.purple_700)
        }

        binding.btnCreated.setOnClickListener {
            binding.activityView.adapter = createdAdapter
            binding.btnFollow.setBackgroundResource(R.color.grey)
            binding.btnCreated.setBackgroundResource(R.color.purple_700)
        }

        profileViewModel.user.observe(viewLifecycleOwner){ user ->
            context?.apply {
                AuthManager.setUser(this, user)
                binding.username.text = user?.username
                binding.fullname.text = user?.fullName
                user?.username?.getAvatarUrl {
                    Log.e("avatar", it)
                    Glide.with(this)
                        .load(it)
                        .placeholder(R.drawable.ic_default_avatar)
                        .into(binding.ivProfilePic)
                }
            }
        }

        profileViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileViewState.Progress -> onProgress(state.isLoading)
                is ProfileViewState.FollowingPost -> showFollowingPost(state.postlist)
                is ProfileViewState.CreatedPost -> showCreatedPost(state.postlist)
            }
        }
    }

    private fun showCreatedPost(postlist: List<Post>) {
        createdAdapter.setData(postlist)
    }

    private fun showFollowingPost(postlist: List<Post>) {
        followingAdapter.setData(postlist)
    }

    private fun onProgress(loading: Boolean) {
        if (loading) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getProfile()
    }
}