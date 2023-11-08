package com.arif.mendakuy.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arif.mendakuy.R
import com.arif.mendakuy.data.model.Guide
import com.arif.mendakuy.databinding.ItemGuideBinding
import com.arif.mendakuy.ui.detailguide.DetailGuideActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class GuideAdapter(private val mList: MutableList<Guide>) :
    RecyclerView.Adapter<GuideAdapter.ViewHolder>() {

        fun setData(list: List<Guide>) {
            mList.clear()
            mList.addAll(list)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_guide, parent, false)

            return ViewHolder(
                ItemGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = mList[position]

            holder.bind(data)

        }

        override fun getItemCount(): Int {
            return mList.size
        }

        inner class ViewHolder(val binding: ItemGuideBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(data: Guide) {

                binding.apply {

//                mountName.text = data.mountName
//                mountOfElevation.text = data.elevation
                    Glide.with(ivGuideBanner).load(data.guideImg).fitCenter().transform(
                        RoundedCorners(24)).into(ivGuideBanner);

                    btnDetail.setOnClickListener {
                        val intent = Intent(it.context, DetailGuideActivity::class.java)
                        intent.putExtra("id", data.id)
                        it.context.startActivity(intent)
                    }

                }
            }
        }
}