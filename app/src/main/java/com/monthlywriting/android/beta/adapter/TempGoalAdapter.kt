package com.monthlywriting.android.beta.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.ItemTempGoalBinding
import com.monthlywriting.android.beta.util.setTextViewColor

class TempGoalAdapter(private val deleteItem: (Int) -> Unit) :
    RecyclerView.Adapter<TempGoalAdapter.TempGoalViewHolder>() {

    lateinit var context: Context

    private val diffUtilCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempGoalViewHolder {
        context = parent.context
        return TempGoalViewHolder(ItemTempGoalBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: TempGoalViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int = differ.currentList.size + 1

    inner class TempGoalViewHolder(private val binding: ItemTempGoalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData() {
            when (adapterPosition){
                differ.currentList.size -> {
                    binding.tvGoal.text = context.resources.getString(R.string.hint_extra_goal)
                    setTextViewColor(context, binding.tvGoal, R.color.color_979797)
                }
                else -> {
                    binding.tvGoal.text = differ.currentList[adapterPosition]
                    binding.ivDelete.setOnClickListener {
                        deleteItem(adapterPosition)
                    }
                }
            }
        }
    }
}