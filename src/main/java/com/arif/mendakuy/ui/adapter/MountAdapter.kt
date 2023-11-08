package com.arif.mendakuy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arif.mendakuy.R
import com.arif.mendakuy.data.model.Mount
import com.arif.mendakuy.databinding.ItemMountBinding
import com.bumptech.glide.Glide

class MountAdapter(private val mList: MutableList<Mount>) :
    RecyclerView.Adapter<MountAdapter.ViewHolder>() {

    fun setData(list: List<Mount>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    var mountList: Mount? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mount, parent, false)

        return ViewHolder(
            ItemMountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = mList[position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(val binding: ItemMountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Mount) {

            binding.apply {

                Glide.with(ivMount).load(data.mountLogo).into(ivMount);
                mountName.text = data.mountName
                if (data.code == mountList?.code) {
                    mountName.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.white
                        )
                    )
                } else {
                    mountName.setTextColor(ContextCompat.getColor(root.context, R.color.grey))
                }
                root.setOnClickListener {
                    mountList = data
                    notifyDataSetChanged()
                }
            }
        }
    }
}

