package com.monthlywriting.android.beta.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.ItemWritingGoalBinding
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.ui.writing.main.WritingMainFragmentDirections
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentMonth

class WritingGoalAdapter(private val selectGoal: (Int) -> Unit) :
    RecyclerView.Adapter<WritingGoalAdapter.WritingGoalViewHolder>() {

    lateinit var context: Context

    private val diffUtilCallback = object : DiffUtil.ItemCallback<MonthlyGoal>() {
        override fun areItemsTheSame(oldItem: MonthlyGoal, newItem: MonthlyGoal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MonthlyGoal, newItem: MonthlyGoal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WritingGoalViewHolder {
        context = parent.context
        return WritingGoalViewHolder(ItemWritingGoalBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: WritingGoalViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int = differ.currentList.size + 1

    inner class WritingGoalViewHolder(private val binding: ItemWritingGoalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData() {

            when (bindingAdapterPosition) {
                differ.currentList.size -> {
                    binding.apply {
                        tvGoal.text =
                            context.resources.getString(R.string.text_item_monthly_writing)
                        root.setOnClickListener {
                            val action =
                                WritingMainFragmentDirections.openWritingMonth(currentMonth)
                            it.findNavController().navigate(action)
                        }
                    }
                }
                else -> {
                    binding.apply {
                        tvGoal.text = differ.currentList[bindingAdapterPosition].goal
                        root.setOnClickListener {
                            selectGoal(bindingAdapterPosition)
                            it.findNavController().navigate(R.id.open_writing_short)
                        }

                        val rating = differ.currentList[bindingAdapterPosition].rating
                        if (rating.getValue(rating.keys.first()) != "") {
                            binding.ivWriting.setImageDrawable(ResourcesCompat.getDrawable(
                                context.resources, R.drawable.text_btn_monthly_written, null
                            ))
                        }
                    }
                }
            }
        }
    }
}