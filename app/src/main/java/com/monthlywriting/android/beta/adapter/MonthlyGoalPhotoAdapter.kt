package com.monthlywriting.android.beta.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.ItemGoalPhotoBinding
import com.monthlywriting.android.beta.util.getBitmapFromPath

class MonthlyGoalPhotoAdapter(
    private val launchGallery: () -> Unit,
    private val openMomentz: (Int) -> Unit,
    private val isEditable: Boolean,
) : RecyclerView.Adapter<MonthlyGoalPhotoAdapter.MonthlyGoalPhotoViewHolder>() {

    var width: Int = 0

    private val diffUtilCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyGoalPhotoViewHolder {
        val binding = ItemGoalPhotoBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)

        width = ((parent.width -
                3 * parent.context.resources.getDimension(R.dimen.horizontal_recycler_view_margin)) * 0.25).toInt()
        return MonthlyGoalPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthlyGoalPhotoViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MonthlyGoalPhotoViewHolder(private val binding: ItemGoalPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData() {
            binding.root.layoutParams = ViewGroup.LayoutParams(width, width / 24 * 33)
            Log.d("test", width.toString())

            when (isEditable) {
                true -> {
                    if (bindingAdapterPosition == 0) {
                        binding.ivPhoto.setImageDrawable(null)
                        binding.ivAddPhoto.visibility = View.VISIBLE
                        binding.root.setOnClickListener {
                            launchGallery()
                        }
                    } else {
                        Glide.with(itemView)
                            .load(getBitmapFromPath(differ.currentList[bindingAdapterPosition]))
                            .into(binding.ivPhoto)

                        binding.root.setOnClickListener {
                            openMomentz(bindingAdapterPosition - 1)
                        }
                    }
                }
                false -> {
                    Glide.with(itemView)
                        .load(getBitmapFromPath(differ.currentList[bindingAdapterPosition]))
                        .into(binding.ivPhoto)

                    binding.root.setOnClickListener {
                        openMomentz(bindingAdapterPosition)
                    }
                }
            }
        }
    }
}