package com.monthlywriting.android.beta.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.ItemGuideBinding

class GuideAdapter : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        context = parent.context
        return GuideViewHolder(ItemGuideBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int = 3

    inner class GuideViewHolder(private val binding: ItemGuideBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData() {
            when (bindingAdapterPosition) {
                0 -> {
                    binding.ivGuide.setImageDrawable(ResourcesCompat.getDrawable(
                        context.resources, R.drawable.img_guide_1, null
                    ))
                }
                1 -> {
                    binding.ivGuide.setImageDrawable(ResourcesCompat.getDrawable(
                        context.resources, R.drawable.img_guide_2, null
                    ))
                }
                2 -> {
                    binding.ivGuide.setImageDrawable(ResourcesCompat.getDrawable(
                        context.resources, R.drawable.img_guide_3, null
                    ))
                }

            }
        }
    }
}