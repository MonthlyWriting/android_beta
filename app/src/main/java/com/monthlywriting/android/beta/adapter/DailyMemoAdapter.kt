package com.monthlywriting.android.beta.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monthlywriting.android.beta.databinding.ItemMemoBinding
import com.monthlywriting.android.beta.model.DailyMemo
import com.monthlywriting.android.beta.ui.goal.detail.GoalDetailFragmentDirections
import java.text.SimpleDateFormat
import java.util.*

class DailyMemoAdapter(private val isEditable: Boolean) :
    RecyclerView.Adapter<DailyMemoAdapter.DailyMemoViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<DailyMemo>() {
        override fun areItemsTheSame(oldItem: DailyMemo, newItem: DailyMemo): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: DailyMemo, newItem: DailyMemo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyMemoViewHolder {
        return DailyMemoViewHolder(ItemMemoBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: DailyMemoViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class DailyMemoViewHolder(private val binding: ItemMemoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData() {
            binding.apply {
                tvMemo.text = differ.currentList[bindingAdapterPosition].memo
                tvTimestamp.text = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(
                    differ.currentList[bindingAdapterPosition].timestamp
                )

                if (isEditable) {
                    root.setOnClickListener {
                        val action =
                            GoalDetailFragmentDirections.openMemoEdit(bindingAdapterPosition)
                        it.findNavController().navigate(action)
                    }
                }

            }
        }
    }
}