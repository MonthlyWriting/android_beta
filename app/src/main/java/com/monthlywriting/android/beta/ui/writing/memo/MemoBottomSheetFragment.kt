package com.monthlywriting.android.beta.ui.writing.memo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.WritingActivityViewModel
import com.monthlywriting.android.beta.adapter.DailyMemoAdapter
import com.monthlywriting.android.beta.databinding.FragmentMemoBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemoBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentMemoBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel: WritingActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMemoBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.transparent_dialog)
        dialog.apply {
            setCanceledOnTouchOutside(true)
            behavior.isDraggable = false
            window?.attributes?.windowAnimations = R.style.animation_slide_up
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerView()

        binding.rlKnob.setOnClickListener {
            dismiss()
        }
    }

    private fun setRecyclerView() {
        binding.rvGoal.apply {
            adapter = DailyMemoAdapter(isEditable = false)
            layoutManager = LinearLayoutManager(requireContext())
        }

        (binding.rvGoal.adapter as DailyMemoAdapter).differ.submitList(activityViewModel.selectedGoal.value!!.memo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}