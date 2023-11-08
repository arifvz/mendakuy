package com.arif.mendakuy.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arif.mendakuy.R
import com.arif.mendakuy.data.model.Chat
import com.arif.mendakuy.databinding.ItemChatBinding
import com.arif.mendakuy.ui.chat.ContentChatActivity
import com.bumptech.glide.Glide

class ChatAdapter(private val chatList: MutableList<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    fun setData(list: List<Chat>) {
        chatList.clear()
        chatList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)

        return ViewHolder(
            ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = chatList[position]

        holder.bind(data)

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    inner class ViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Chat) {
            binding.apply {
                    tvGroupName.text = data.groupName
                    tvRecentMessage.text = data.mount?.mountName
                    Glide.with(ivMount).load(data.mount?.mountLogo).circleCrop().into(ivMount)

                itemView.setOnClickListener { view ->
                    val intent = Intent(view.context, ContentChatActivity::class.java)
                    intent.putExtra("groupId", data.groupId)
                    view.context.startActivity(intent)
                }

            }
        }
    }

}

