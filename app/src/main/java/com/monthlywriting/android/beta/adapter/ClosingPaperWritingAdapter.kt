package com.monthlywriting.android.beta.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.ItemClosingPaperWritingBinding
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.util.setTextViewColor

class ClosingPaperWritingAdapter(
    private val isEditable: Boolean,
    private val selectIndex: (Int) -> Unit,
    private val saveTempList: (Int, String) -> Unit,
) : RecyclerView.Adapter<ClosingPaperWritingAdapter.ClosingPaperWritingViewHolder>() {

    private lateinit var context: Context

    private val diffUtilCallback = object : DiffUtil.ItemCallback<MonthlyGoal>() {
        override fun areItemsTheSame(oldItem: MonthlyGoal, newItem: MonthlyGoal): Boolean {
            return oldItem.rating == newItem.rating
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
        context = parent.context

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
            val item = differ.currentList[bindingAdapterPosition]
            binding.etWriting.setText(item.writing ?: "")

            if (isEditable) {
                binding.etWriting.visibility = View.VISIBLE
            } else {
                binding.tvWriting.visibility = View.VISIBLE
            }

            if (bindingAdapterPosition == differ.currentList.lastIndex) {
                binding.ivMemoDrawer.visibility = View.GONE
            }

            binding.tvGoal.text = item.goal
            if (item.writing.isNullOrBlank()) {
                binding.tvWriting.visibility = View.GONE
            } else {
                binding.tvWriting.text = item.writing
            }

            when (item.rating.keys.first()) {
                "emoji" -> {
                    if (item.rating["emoji"].toString() != "") {
                        setTextViewColor(context, binding.tvEvaluation, R.color.black)
                        binding.tvEvaluation.text = item.rating["emoji"].toString()
                    } else {
                        setTextViewColor(context, binding.tvEvaluation, R.color.color_click)
                    }
                }
                "percentage" -> {
                    setTextViewColor(context, binding.tvEvaluation, R.color.black)
                    binding.tvEvaluation.text = item.rating["percentage"].toString()
                }
                "star" -> {
                    setTextViewColor(context, binding.tvEvaluation, R.color.black)
                    binding.tvEvaluation.text = item.rating["star"].toString()
                }
                "level" -> {
                    setTextViewColor(context, binding.tvEvaluation, R.color.black)
                    binding.tvEvaluation.text = item.rating["level"].toString()
                }
            }

            binding.ivMemoDrawer.setOnClickListener {
                selectPosition()
                it.findNavController().navigate(R.id.open_memo_bottom_sheet)
            }

            binding.tvEvaluation.setOnClickListener {
                selectPosition()
                it.findNavController().navigate(R.id.open_writing_short)
            }

            binding.etWriting.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    saveTempList(bindingAdapterPosition, p0.toString())
                }
            })
        }

        private fun selectPosition() {
            if (bindingAdapterPosition == differ.currentList.lastIndex) {
                selectIndex(-1)
            } else {
                selectIndex(bindingAdapterPosition)
            }
        }
    }
}