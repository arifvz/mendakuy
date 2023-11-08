package com.arif.mendakuy.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.arif.mendakuy.App
import com.arif.mendakuy.R
import com.arif.mendakuy.data.K
import com.arif.mendakuy.data.model.Mount
import com.arif.mendakuy.data.model.Post
import com.arif.mendakuy.databinding.ItemPostBinding
import com.arif.mendakuy.ui.detailpost.DetailPostActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class PostAdapter(private val mList: MutableList<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    fun setData(list: List<Post>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    var mountList : Mount? = null
    var callback: Callback? = null
    var isShowJoin: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)

        return ViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Post) {
            binding.apply {
                val date: String? = try {
                    data.dateStart?.let {
                        SimpleDateFormat("dd MMM yyyy").format(it)
                    }
                } catch (e: Exception) { "-"
                }
                titlePost.text = data.postTitle
                dateStart.text = date
                Glide.with(ivBackground).load(data.mount?.mountImg).centerCrop().into(ivBackground);
                btnJoin.isVisible = isShowJoin

                val participant =
                    data.participants?.firstOrNull { it.user?.username == App.getUserName() }
                if (participant != null) {
                    btnJoin.isEnabled = false
                    btnJoin.text = participant.status
                } else {
                    btnJoin.isEnabled = true
                    btnJoin.text = K.JOIN
                }
                itemView.setOnClickListener { view ->
                    val intent = Intent(view.context, DetailPostActivity::class.java)
                    intent.putExtra("id", data.id)
                    view.context.startActivity(intent)
                }
                btnJoin.setOnClickListener {
                    callback?.onClickJoin(data)
                }

            }
        }
    }

    interface Callback {
        fun onClickJoin(post: Post)
    }
}

