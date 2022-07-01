package com.monthlywriting.android.beta.ui.writing.closing

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
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
import com.monthlywriting.android.beta.databinding.FragmentClosingPaperBinding
import com.monthlywriting.android.beta.di.App
import com.monthlywriting.android.beta.ui.goal.detail.PhotoDetailFragment
import com.monthlywriting.android.beta.ui.main.home.HomeFragmentDirections
import com.monthlywriting.android.beta.util.CurrentInfo
import com.monthlywriting.android.beta.util.CustomTypefaceSpan

class ClosingPaperFragment : Fragment() {

    private var _binding: FragmentClosingPaperBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel: WritingActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentClosingPaperBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTextFont()
        setRecyclerView()
        handleBackAction()
    }

    private fun setTextFont() {
        val font = CustomTypefaceSpan(Typeface.create(ResourcesCompat.getFont(
            requireContext(), R.font.font_pretendard_semibold), Typeface.NORMAL))

        binding.tvTitle.text =
            SpannableStringBuilder(resources.getString(
                R.string.text_closing_paper_chat,
                App.prefs.namePref,
                (activity as WritingActivity).getCurrentMonth()
            )).also {
                it.setSpan(font, 0, it.split("님을")[0].length, 0)
                it.setSpan(font, it.length - 2, it.length, 0)
            }
    }

    private fun setRecyclerView() {
        binding.rvGoal.apply {
            adapter = ClosingPaperWritingAdapter(
                isEditable = false,
                selectIndex = { index -> selectIndex(index) },
                saveTempList = { _, _ -> }
            )
            layoutManager = LinearLayoutManager(requireContext())
        }

        activityViewModel.monthlyGoalList.observe(viewLifecycleOwner) {
            (binding.rvGoal.adapter as ClosingPaperWritingAdapter).differ.submitList(it)
        }

        binding.rvPhoto.apply {
            adapter = MonthlyGoalPhotoAdapter(
                launchGallery = {},
                openMomentz = { position -> openMomentz(position) },
                isEditable = false
            )
            layoutManager = GridLayoutManager(requireContext(), 4)
            addItemDecoration(GridMarginDecoration(requireContext()))
        }

        (activity as WritingActivity).getAllPhotoList()
        activityViewModel.photoList.observe(viewLifecycleOwner) {
            (binding.rvPhoto.adapter as MonthlyGoalPhotoAdapter).differ.submitList(it)
        }
    }

    private fun handleBackAction() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (activity as WritingActivity).finish()

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.putExtra("isCollectionAdded", true)
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

    private fun openMomentz(position: Int) {
        val fragment = PhotoDetailFragment.newInstance(
            position,
            activityViewModel.photoList.value!!,
            "월말결산"
        )

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.full_container, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}