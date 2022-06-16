package com.monthlywriting.android.beta.ui.goal.memo.edit

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.FragmentMemoEditBinding
import com.monthlywriting.android.beta.activity.MainActivity
import com.monthlywriting.android.beta.model.DailyMemo
import com.monthlywriting.android.beta.ui.goal.detail.GoalDetailViewModel

class MemoEditFragment : Fragment() {

    private var _binding: FragmentMemoEditBinding? = null
    private val binding get() = _binding!!

    private val goalDetailViewModel: GoalDetailViewModel by activityViewModels()
    private val viewModel: MemoEditViewModel by viewModels()

    private val args: MemoEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_memo_edit, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).setToolbarTitle(resources.getString(R.string.title_memo_edit))
        (activity as MainActivity).toggleDeleteBtn(true){
            val builder = AlertDialog.Builder(requireContext())
            builder
                .setTitle(requireContext().resources.getString(R.string.dialog_delete_memo_title))
                .setMessage(requireContext().resources.getString(R.string.dialog_delete_memo_message))
                .setPositiveButton(requireContext().resources.getString(R.string.dialog_yes)) { _, _ ->
                    goalDetailViewModel.deleteMemo(args.index)
                     findNavController().navigateUp()
                }
                .setNegativeButton(requireContext().resources.getString(R.string.dialog_no)) { _, _ ->

                }
                .show()
        }

        setMemo()

        binding.etMemo.requestFocus()
        binding.btnSave.setOnClickListener {
            saveMemo()
        }
    }

    private fun setMemo() {
        viewModel.memo.value = goalDetailViewModel.memo.value?.get(args.index)?.memo
    }

    private fun saveMemo() {
        val newMemo = viewModel.memo.value
        if (newMemo.isNullOrEmpty()) {
            Toast.makeText(requireContext(), R.string.toast_no_memo_input, Toast.LENGTH_SHORT)
                .show()
        } else {
            goalDetailViewModel.updateMemo(args.index, DailyMemo(
                timestamp = System.currentTimeMillis(),
                memo = newMemo
            ))
        }
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).toggleDeleteBtn(false) {}
        _binding = null
    }
}