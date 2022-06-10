package com.monthlywriting.android.beta.ui.writing.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.WritingActivity
import com.monthlywriting.android.beta.databinding.FragmentWritingMonthBinding
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentMonth
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentYear
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WritingMonthFragment : Fragment() {

    private var _binding: FragmentWritingMonthBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WritingMonthViewModel by viewModels()
    private val args: WritingMonthFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_writing_month, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as WritingActivity).toggleSaveBtn(true) {
            saveToDataBase()
            findNavController().navigate(R.id.nav_writing_main)
        }
        viewModel.getWriting(currentYear, currentMonth)
    }

    private fun saveToDataBase() {
        viewModel.saveWriting(
            year = currentYear,
            month = currentMonth,
            writing = viewModel.writing.value!!,
            photoList = mutableListOf()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as WritingActivity).toggleSaveBtn(false) {}
        _binding = null
    }
}