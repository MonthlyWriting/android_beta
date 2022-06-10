package com.monthlywriting.android.beta.ui.goal.detail

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.MainActivity
import com.monthlywriting.android.beta.adapter.DailyMemoAdapter
import com.monthlywriting.android.beta.adapter.HorizontalMarginDecoration
import com.monthlywriting.android.beta.adapter.MonthlyGoalPhotoAdapter
import com.monthlywriting.android.beta.databinding.FragmentGoalDetailBinding
import com.monthlywriting.android.beta.util.checkPermission
import com.monthlywriting.android.beta.util.getGalleryLauncher
import com.monthlywriting.android.beta.util.launchGalleryLauncher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalDetailFragment : Fragment() {

    private var _binding: FragmentGoalDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GoalDetailViewModel by activityViewModels()
    private val args: GoalDetailFragmentArgs by navArgs()

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGoalDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        when (args.isEditable) {
            false -> {
                binding.tvAddMemo.visibility = View.GONE
                binding.ivDelete.visibility = View.GONE
            }
            true -> {
                (activity as MainActivity).setToolbarTitle(resources.getString(R.string.title_goal_detail))
            }
        }
        setRecyclerView()
        setGalleryLauncher()
        setCurrentData()
        setFunction()
    }

    private fun setRecyclerView() {
        binding.rvPhoto.apply {
            adapter = MonthlyGoalPhotoAdapter(
                launchGallery = {
                    checkPermission(requireActivity()) {
                        launchGalleryLauncher(galleryLauncher)
                    }
                },
                openMomentz = { position -> openMomentz(position) },
                isEditable = args.isEditable
            )
            layoutManager = LinearLayoutManager(requireContext()).also {
                it.orientation = LinearLayoutManager.HORIZONTAL
            }
            addItemDecoration(HorizontalMarginDecoration(requireContext()))
            smoothScrollToPosition(0)
        }


        binding.rvMemo.apply {
            adapter = DailyMemoAdapter(isEditable = args.isEditable)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun openMomentz(position: Int) {
        val fragment = PhotoDetailFragment.newInstance(
            position,
            viewModel.photoList.value!!,
            viewModel.monthlyGoal.value!!.goal
        )

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.full_container, fragment)
            .commit()
    }

    private fun setGalleryLauncher() {
        galleryLauncher =
            getGalleryLauncher(requireContext(), this)
            { filePath -> viewModel.insertPhoto(filePath) }
    }

    private fun setCurrentData() {
        viewModel.getCurrentMonthlyGoal(args.id)
        viewModel.monthlyGoal.observe(viewLifecycleOwner) {
            binding.tvGoal.text = it.goal
        }

        viewModel.photoList.observe(viewLifecycleOwner) {
            if (args.isEditable) {
                val list = it.toMutableList().also { list -> list.add(0, "") }
                (binding.rvPhoto.adapter as MonthlyGoalPhotoAdapter).differ.submitList(list)
            } else {
                (binding.rvPhoto.adapter as MonthlyGoalPhotoAdapter).differ.submitList(it)
            }
        }

        viewModel.memo.observe(viewLifecycleOwner) {
            (binding.rvMemo.adapter as DailyMemoAdapter).differ.submitList(it)
        }
    }

    private fun setFunction() {
        binding.tvAddMemo.setOnClickListener {
            findNavController().navigate(R.id.open_memo_add)
        }

        binding.ivDelete.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder
                .setTitle(requireContext().resources.getString(R.string.dialog_delete_goal_title))
                .setMessage(requireContext().resources.getString(R.string.dialog_delete_goal_message))
                .setPositiveButton(requireContext().resources.getString(R.string.dialog_yes)) { _, _ ->
                    viewModel.deleteGoal(viewModel.monthlyGoal.value!!.id)
                    findNavController().navigateUp()
                }
                .setNegativeButton(requireContext().resources.getString(R.string.dialog_no)) { _, _ ->

                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}