package com.monthlywriting.android.beta.ui.writing.main

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.WritingActivity
import com.monthlywriting.android.beta.activity.WritingActivityViewModel
import com.monthlywriting.android.beta.adapter.WritingGoalAdapter
import com.monthlywriting.android.beta.databinding.FragmentWritingMainBinding
import com.monthlywriting.android.beta.util.CustomTypefaceSpan
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WritingMainFragment : Fragment() {

    private var _binding: FragmentWritingMainBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel: WritingActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWritingMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setChatText()
        setRecyclerView()
        setFunction()
    }

    private fun setChatText() {
        val text =
            SpannableStringBuilder(resources.getString(R.string.text_monthly_writing_chat_main,
                (activity as WritingActivity).getCurrentMonth()))

        val font = CustomTypefaceSpan(Typeface.create(ResourcesCompat.getFont(
            requireContext(), R.font.font_pretendard_semibold), Typeface.NORMAL))

        text.setSpan(font, 9, 17, 0)

        binding.tvChatMain.text = text
    }

    private fun setRecyclerView() {
        binding.rvGoal.apply {
            adapter = WritingGoalAdapter { index -> activityViewModel.selectGoal(index) }
            layoutManager = LinearLayoutManager(requireContext())
        }

        activityViewModel.monthlyGoalList.observe(viewLifecycleOwner) {
            (binding.rvGoal.adapter as WritingGoalAdapter).differ.submitList(it)
        }
    }

    private fun setFunction() {
        binding.btnClosingPaper.setOnClickListener {
            it.findNavController().navigate(R.id.nav_closing_paper)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
