package com.monthlywriting.android.beta.ui.writing.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.MainActivity
import com.monthlywriting.android.beta.activity.WritingActivity
import com.monthlywriting.android.beta.activity.WritingActivityViewModel
import com.monthlywriting.android.beta.adapter.ClosingPaperWritingAdapter
import com.monthlywriting.android.beta.adapter.GridMarginDecoration
import com.monthlywriting.android.beta.adapter.MonthlyGoalPhotoAdapter
import com.monthlywriting.android.beta.databinding.FragmentWritingMainBinding
import com.monthlywriting.android.beta.ui.goal.detail.PhotoDetailFragment
import com.monthlywriting.android.beta.util.checkPermission
import com.monthlywriting.android.beta.util.getGalleryLauncher
import com.monthlywriting.android.beta.util.launchGalleryLauncher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WritingMainFragment : Fragment() {

    private var _binding: FragmentWritingMainBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel: WritingActivityViewModel by activityViewModels()

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWritingMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setGalleryLauncher()
        setRecyclerView()
        setFunction()
        handleBackAction()
    }

    private fun setRecyclerView() {
        binding.rvGoal.apply {
            adapter = ClosingPaperWritingAdapter(
                isEditable = true,
                selectIndex = { index -> selectIndex(index) },
                saveTempList = { index, writing -> saveTempList(index, writing) }
            )
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }

        activityViewModel.monthlyGoalList.observe(viewLifecycleOwner) {
            (binding.rvGoal.adapter as ClosingPaperWritingAdapter).apply {
                differ.submitList(it)
                notifyDataSetChanged()
            }
        }

        binding.rvPhoto.apply {
            adapter = MonthlyGoalPhotoAdapter(
                launchGallery = {
                    checkPermission(requireActivity()) {
                        launchGalleryLauncher(galleryLauncher)
                    }
                },
                openMomentz = { position -> openMomentz(position) },
                isEditable = true
            )
            layoutManager = GridLayoutManager(requireContext(), 4)
            addItemDecoration(GridMarginDecoration(requireContext()))
            itemAnimator = null
        }

        (activity as WritingActivity).getAllPhotoList()
        activityViewModel.photoList.observe(viewLifecycleOwner) {
            val list = it.toMutableList().also { list -> list.add(0, "") }
            (binding.rvPhoto.adapter as MonthlyGoalPhotoAdapter).differ.submitList(list)
        }
    }

    private fun setGalleryLauncher() {
        galleryLauncher =
            getGalleryLauncher(requireContext(), this)
            { filePath -> activityViewModel.insertPhoto(filePath) }
    }

    private fun openMomentz(position: Int) {
        val fragment = PhotoDetailFragment.newInstance(
            position,
            activityViewModel.photoList.value!!,
            "월말결산" // 화면에 따른 제목 변경을 momentz 에서 사용하는 방법 ?
        )

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.full_container, fragment)
            .commit()
    }

    private fun setFunction() {
        binding.tvSave.setOnClickListener {
            saveToDatabase()
        }
    }

    private fun saveToDatabase() {
        if (activityViewModel.monthlyGoalTempList.value != null) {
            activityViewModel.saveAll()
            findNavController().navigate(R.id.open_closing_loading)
        } else {
            Toast.makeText(requireContext(),
                resources.getString(R.string.toast_no_content),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleBackAction() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (activity as WritingActivity).finish()

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
            })
    }

    private fun selectIndex(index: Int) {
        activityViewModel.selectedIndex = index
        activityViewModel.getSelectedGoal(
            (activity as WritingActivity).getCurrentYear(),
            (activity as WritingActivity).getCurrentMonth()
        )
    }

    private fun saveTempList(index: Int, writing: String) {
        activityViewModel.saveTempWriting(index, writing)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
