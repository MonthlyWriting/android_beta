package com.monthlywriting.android.beta.ui.main.collection.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.MainActivity
import com.monthlywriting.android.beta.adapter.MonthlyGoalAdapter
import com.monthlywriting.android.beta.databinding.FragmentCollectionMonthlyBinding
import com.monthlywriting.android.beta.ui.main.collection.CollectionViewModel

class CollectionMonthlyFragment : Fragment() {

    private var _binding: FragmentCollectionMonthlyBinding? = null
    private val binding get() = _binding!!

    private val collectionViewModel: CollectionViewModel by activityViewModels()

    private val args: CollectionMonthlyFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCollectionMonthlyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).setToolbarTitle(resources.getStringArray(R.array.month_name)[args.month - 1])
        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.rvGoal.apply {
            adapter = MonthlyGoalAdapter { position -> openDetailWithId(position) }
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }

        collectionViewModel.getMonthlyGoalList(args.year, args.month)
        collectionViewModel.monthlyGoalList.observe(viewLifecycleOwner) {
            (binding.rvGoal.adapter as MonthlyGoalAdapter).differ.submitList(it)
            binding.tvGoalNum.text = resources.getString(R.string.text_goal_num, it.size)
        }
    }

    private fun openDetailWithId(position: Int) {
        val selectedId = collectionViewModel.monthlyGoalList.value!![position].id
        val action = CollectionMonthlyFragmentDirections.openGoalDetail(selectedId, false)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}