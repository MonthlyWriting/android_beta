package com.monthlywriting.android.beta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monthlywriting.android.beta.databinding.ItemClosingPaperWritingBinding
import com.monthlywriting.android.beta.model.MonthlyGoal

class ClosingPaperWritingAdapter :
    RecyclerView.Adapter<ClosingPaperWritingAdapter.ClosingPaperWritingViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<MonthlyGoal>() {
        override fun areItemsTheSame(oldItem: MonthlyGoal, newItem: MonthlyGoal): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: MonthlyGoal, newItem: MonthlyGoal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ClosingPaperWritingViewHolder {
        return ClosingPaperWritingViewHolder(ItemClosingPaperWritingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ClosingPaperWritingViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ClosingPaperWritingViewHolder(private val binding: ItemClosingPaperWritingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData() {
            val item = differ.currentList[adapterPosition]

            binding.tvGoal.text = item.goal
            if (item.writing.isNullOrBlank()) {
                binding.tvWriting.visibility = View.GONE
            } else {
                binding.tvWriting.text = item.writing
            }


            when (item.rating.keys.first()) {
                "emoji" -> {
                    binding.tvEvaluation.text = item.rating["emoji"].toString()
                }
                "percentage" -> {
                    binding.tvEvaluation.text = item.rating["percentage"].toString()
                }
                "star" -> {
                    binding.tvEvaluation.text = item.rating["star"].toString()
                }
                "level" -> {
                    binding.tvEvaluation.text = item.rating["level"].toString()
                }
            }
        }
    }
}