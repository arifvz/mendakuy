package com.arif.mendakuy.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.arif.mendakuy.App.Companion.getAvatarUrl
import com.arif.mendakuy.R
import com.arif.mendakuy.data.K
import com.arif.mendakuy.data.model.Participant
import com.arif.mendakuy.databinding.ItemParticipantBinding
import com.arif.mendakuy.ui.detailpost.DetailPostViewModel
import com.bumptech.glide.Glide

class ParticipantAdapter(private val mList: MutableList<Participant>) :
    RecyclerView.Adapter<ParticipantAdapter.ViewHolder>() {

    private lateinit var participantViewModel: DetailPostViewModel
    var callback: Callback? = null

    fun setData(list: List<Participant>?) {
        mList.clear()
        list?.let {
            mList.addAll(list)
        }
        notifyDataSetChanged()
    }

    var isShowButton: Boolean = true
    var isShowText: Boolean = true


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_participant, parent, false)

        return ViewHolder(
            ItemParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = mList[position]

        holder.bind(data)

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(val binding: ItemParticipantBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun bind(data: Participant) {
            binding.apply {

                data.user?.username?.getAvatarUrl {
                    Log.e("url", it)
                    Glide.with(binding.ivProfilePic)
                        .load(it)
                        .placeholder(R.drawable.ic_default_avatar)
                        .into(binding.ivProfilePic)
                }

                tvUsernamePlayer.text = data.user?.username
                tvMessage.text = data.message
                tvStatus.text = data.status
                if (data.status.equals(K.STATUS_ACCEPT, true)) {
                    tvStatus.setBackgroundResource(R.drawable.layout_bg_status_accept)
                } else if (data.status.equals(K.STATUS_DECLINE, true)) {
                    tvStatus.setBackgroundResource(R.drawable.layout_bg_status_decline)
                } else {
                    tvStatus.setBackgroundResource(R.drawable.layout_bg_status_request)
                }

                if (isShowButton) {
                    llButton.isVisible = K.STATUS_REQUEST.equals(data.status, ignoreCase = true)
                } else {
                    llButton.visibility = View.GONE
                }

                if(isShowText){
                    tvHiking.isInvisible = K.STATUS_HOST.equals(data.status, ignoreCase = true)
                } else {
                    tvHiking.isVisible = true

                }

                btnAccept.setOnClickListener {
                    callback?.onClickAccept(data)
                }

                btnDecline.setOnClickListener {
                    callback?.onClickDecline(data)
                }

            }
        }
    }

    interface Callback {
        fun onClickAccept(participant: Participant)
        fun onClickDecline(participant: Participant)
    }

}