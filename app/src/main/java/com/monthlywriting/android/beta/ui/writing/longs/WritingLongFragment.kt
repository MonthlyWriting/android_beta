package com.monthlywriting.android.beta.ui.writing.longs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.WritingActivity
import com.monthlywriting.android.beta.activity.WritingActivityViewModel
import com.monthlywriting.android.beta.adapter.HorizontalMarginDecoration
import com.monthlywriting.android.beta.adapter.MonthlyGoalPhotoAdapter
import com.monthlywriting.android.beta.databinding.FragmentWritingLongBinding
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.ui.goal.detail.PhotoDetailFragment
import com.monthlywriting.android.beta.util.checkPermission
import com.monthlywriting.android.beta.util.getGalleryLauncher
import com.monthlywriting.android.beta.util.launchGalleryLauncher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WritingLongFragment : Fragment() {

    private var _binding: FragmentWritingLongBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel: WritingActivityViewModel by activityViewModels()
    private val viewModel: WritingLongViewModel by viewModels()

    private lateinit var currentGoal: MonthlyGoal
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_writing_long, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentGoal = activityViewModel.selectedGoal.value!!

        setGoalData()
        setRecyclerView()
        setGalleryLauncher()
        setFunction()
    }

    private fun setGoalData() {
        val rating = currentGoal.rating
        val value = rating.getValue(rating.keys.first())
        binding.tvEvaluation.text = value.toString()
        binding.tvGoal.text = currentGoal.goal
        viewModel.writing.value = currentGoal.writing
    }

    private fun setRecyclerView() {
        binding.rvPhoto.apply {
            adapter = MonthlyGoalPhotoAdapter(
                launchGallery = {
                    checkPermission(requireActivity()) {
                        launchGalleryLauncher(galleryLauncher)
                    }
                },
                openMomentz = { },
                isEditable = true
            )
            layoutManager = LinearLayoutManager(requireContext()).also {
                it.orientation = LinearLayoutManager.HORIZONTAL
            }
            addItemDecoration(HorizontalMarginDecoration(requireContext()))
            smoothScrollToPosition(0)
        }
    }

    private fun setFunction() {
        binding.tvDrawerDailyMemo.setOnClickListener {
//            it.findNavController().navigate(R.id.open_memo_bottom_sheet)
        }

        (activity as WritingActivity).toggleSaveBtn(true) {
            saveToDataBase()
            findNavController().navigate(R.id.nav_writing_main)
        }
    }

    private fun saveToDataBase() {
        activityViewModel.insertWriting(currentGoal.id, viewModel.writing.value ?: "")
    }

    private fun setGalleryLauncher() {
        galleryLauncher =
            getGalleryLauncher(requireContext(), this)
            { filePath -> activityViewModel.insertPhoto(filePath) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as WritingActivity).toggleSaveBtn(false) {}
        _binding = null
    }
}