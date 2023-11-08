package com.arif.mendakuy.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.arif.mendakuy.App
import com.arif.mendakuy.App.Companion.getAvatarUrl
import com.arif.mendakuy.R
import com.arif.mendakuy.data.model.Messages
import com.arif.mendakuy.databinding.ItemBubbleChatBinding
import com.arif.mendakuy.databinding.ItemBubbleChatSenderBinding
import com.bumptech.glide.Glide

class MessageAdapter(private val messageList: MutableList<Messages>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    fun setData(list: List<Messages>) {
        messageList.clear()
        messageList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 1) {
            return ViewHolder(
                ItemBubbleChatSenderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ViewHolder(
                ItemBubbleChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].username == App.getUserName()) {
            1
        } else {
            0
        }

    }

    inner class ViewHolder(private val viewBinding: ViewBinding) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(position: Int) {
            val data = messageList[position]
            if (getItemViewType(position) == 1) {
                val binding = viewBinding as ItemBubbleChatSenderBinding
                binding.apply {
                    tvFullname.text = data.username.toString()
                    tvMessage.text = data.message.toString()
                    data.username?.getAvatarUrl {
                        Glide.with(ivProfile).load(it).circleCrop()
                            .into(ivProfile)
                    }
                }
            } else {
                val binding = viewBinding as ItemBubbleChatBinding
                binding.apply {
                    tvFullname.text = data.username.toString()
                    tvMessage.text = data.message.toString()
                    data.username?.getAvatarUrl {
                        Glide.with(ivProfile).load(it).circleCrop()
                            .into(ivProfile)
                    }
                }
            }

        }

    }
}

