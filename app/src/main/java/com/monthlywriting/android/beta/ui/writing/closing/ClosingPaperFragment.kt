package com.monthlywriting.android.beta.ui.writing.closing

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.WritingActivity
import com.monthlywriting.android.beta.activity.WritingActivityViewModel
import com.monthlywriting.android.beta.adapter.ClosingPaperWritingAdapter
import com.monthlywriting.android.beta.adapter.GridMarginDecoration
import com.monthlywriting.android.beta.adapter.MonthlyGoalPhotoAdapter
import com.monthlywriting.android.beta.databinding.FragmentClosingPaperBinding
import com.monthlywriting.android.beta.ui.goal.detail.PhotoDetailFragment
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
        setChatText()
        setRecyclerView()
    }

    private fun setChatText() {
        val font = CustomTypefaceSpan(Typeface.create(ResourcesCompat.getFont(
            requireContext(), R.font.font_pretendard_semibold), Typeface.NORMAL))

        binding.tvChatShort1.text =
            SpannableStringBuilder(resources.getString(R.string.text_closing_paper_chat,
                (activity as WritingActivity).getCurrentMonth())).also {
                it.setSpan(font, it.lines()[0].length, it.length, 0)
            }
    }

    private fun setRecyclerView() {
        binding.rvMonthlyWriting.apply {
            adapter = ClosingPaperWritingAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }

        activityViewModel.monthlyGoalList.observe(viewLifecycleOwner) {
            (binding.rvMonthlyWriting.adapter as ClosingPaperWritingAdapter).differ.submitList(it)
        }

        binding.rvMonthlyPhoto.apply {
            adapter = MonthlyGoalPhotoAdapter(
                launchGallery = {},
                openMomentz = { position -> openMomentz(position) },
                isEditable = false
            )
            layoutManager = GridLayoutManager(requireContext(), 4)
            addItemDecoration(GridMarginDecoration(requireContext()))
        }

        (activity as WritingActivity).getAllPhotoList()
        activityViewModel.allPhotoList.observe(viewLifecycleOwner) {
            (binding.rvMonthlyPhoto.adapter as MonthlyGoalPhotoAdapter).differ.submitList(it)
        }
    }

    private fun openMomentz(position: Int) {
        val fragment = PhotoDetailFragment.newInstance(
            position,
            activityViewModel.allPhotoList.value!!,
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