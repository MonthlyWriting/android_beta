package com.monthlywriting.android.beta.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.ItemCollectionBinding
import com.monthlywriting.android.beta.ui.main.collection.CollectionFragmentDirections

class CollectionAdapter(private val year: Int) :
    RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    lateinit var context: Context
    private var mExpandedPosition = -1
    private var previousExpandedPosition = -1

    private val diffUtilCallback = object : DiffUtil.ItemCallback<Boolean>() {
        override fun areItemsTheSame(oldItem: Boolean, newItem: Boolean): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Boolean, newItem: Boolean): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        context = parent.context
        return CollectionViewHolder(ItemCollectionBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class CollectionViewHolder(val binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData() {
            binding.tvMonth.text =
                context.resources.getStringArray(R.array.month_name)[bindingAdapterPosition]
            if (!differ.currentList[bindingAdapterPosition]) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.tvMonth.setBackgroundColor(ContextCompat.getColor(context,
                        R.color.color_DDE0E5))
                } else {
                    binding.tvMonth.setBackgroundColor(context.resources.getColor(R.color.color_DDE0E5))
                }

                binding.tvMonthlyGoal.setOnClickListener {
                    val action =
                        CollectionFragmentDirections.openCollectionMonthly(year,
                            bindingAdapterPosition+ 1)
                    it.findNavController().navigate(action)
                }

                binding.tvMonthlyWriting.setOnClickListener {

                }

                val isExpanded = bindingAdapterPosition == mExpandedPosition
                binding.containerDetail.visibility = if (isExpanded) View.VISIBLE else View.GONE

                if (isExpanded) previousExpandedPosition = bindingAdapterPosition

                binding.rlText.setOnClickListener {
                    mExpandedPosition = if (isExpanded) -1 else bindingAdapterPosition
                    notifyItemChanged(previousExpandedPosition)
                    notifyItemChanged(bindingAdapterPosition)
                }
            }

            if (bindingAdapterPosition == differ.currentList.size - 1) {
                binding.ivTag.visibility = View.VISIBLE
            }
        }
    }
}