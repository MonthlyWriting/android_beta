package com.monthlywriting.android.beta.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monthlywriting.android.beta.databinding.ItemGoalBinding
import com.monthlywriting.android.beta.model.MonthlyGoal

class MonthlyGoalAdapter(private val openDetailWithId: (Int) -> Unit) :
    RecyclerView.Adapter<MonthlyGoalAdapter.MonthlyGoalViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<MonthlyGoal>() {
        override fun areItemsTheSame(oldItem: MonthlyGoal, newItem: MonthlyGoal): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: MonthlyGoal, newItem: MonthlyGoal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyGoalViewHolder {
        return MonthlyGoalViewHolder(ItemGoalBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: MonthlyGoalViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class MonthlyGoalViewHolder(private val binding: ItemGoalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData() {
            binding.tvGoal.text = differ.currentList[adapterPosition].goal
            binding.root.setOnClickListener {
                openDetailWithId(adapterPosition)
            }
        }
    }
}