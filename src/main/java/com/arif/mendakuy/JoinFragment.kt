package com.arif.mendakuy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arif.mendakuy.data.model.Post
import com.arif.mendakuy.databinding.FragmentJoinBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class JoinFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentJoinBinding? = null

    private val binding get() = _binding!!
    var callback: Callback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentJoinBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFloating)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            callback?.onSubmit(binding.etMessage.text.toString())
        }

        post?.let {
            setPost(it)
        }
    }

    private var post: Post? = null
    fun getPost() = post

    fun setPost(post: Post) {
        this.post = post
        _binding?.apply {
            tvMountName.text = post.mount?.mountName.toString()
            Glide.with(ivMount).load(post.mount?.mountLogo).into(ivMount)
        }
    }

    interface Callback {
        fun onSubmit(message: String)
    }

}