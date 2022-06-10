package com.monthlywriting.android.beta.ui.goal.memo.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.FragmentMemoAddBinding
import com.monthlywriting.android.beta.activity.MainActivity
import com.monthlywriting.android.beta.model.DailyMemo
import com.monthlywriting.android.beta.ui.goal.detail.GoalDetailViewModel

class MemoAddFragment : Fragment() {

    private var _binding: FragmentMemoAddBinding? = null
    private val binding get() = _binding!!

    private val goalDetailViewModel: GoalDetailViewModel by activityViewModels()
    private val viewModel: MemoAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_memo_add, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).setToolbarTitle(resources.getString(R.string.title_memo_add))

        binding.etMemo.requestFocus()
        binding.btnSave.setOnClickListener {
            saveMemo()
        }
    }

    private fun saveMemo() {
        val newMemo = viewModel.memo.value
        if (newMemo.isNullOrEmpty()) {
            Toast.makeText(requireContext(), R.string.toast_no_memo_input, Toast.LENGTH_SHORT)
                .show()
        } else {
            goalDetailViewModel.insertMemo(DailyMemo(
                timestamp = System.currentTimeMillis(),
                memo = newMemo
            ))
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}