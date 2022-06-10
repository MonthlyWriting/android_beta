package com.monthlywriting.android.beta.ui.main.collection

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.MainActivity
import com.monthlywriting.android.beta.adapter.CollectionAdapter
import com.monthlywriting.android.beta.databinding.FragmentCollectionBinding
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentMonth
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentYear
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionFragment : Fragment() {

    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CollectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCollectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).setToolbarTitle(resources.getString(R.string.title_collection))
        setYear()
        setRecyclerView()
    }

    private fun setYear() {
        binding.tvYear.text = currentYear.toString()
    }

    private fun setRecyclerView() {
        binding.rvCollection.apply {
            adapter = CollectionAdapter(currentYear)
            layoutManager = LinearLayoutManager(requireContext()).also {
                it.reverseLayout = true
            }
        }

        viewModel.getIsNullList(currentYear)
        viewModel.isNullList.observe(viewLifecycleOwner) {
            (binding.rvCollection.adapter as CollectionAdapter).differ
                .submitList(it.subList(0, currentMonth))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}