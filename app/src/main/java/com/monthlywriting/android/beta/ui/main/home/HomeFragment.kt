package com.monthlywriting.android.beta.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.adapter.MonthlyGoalAdapter
import com.monthlywriting.android.beta.databinding.FragmentHomeBinding
import com.monthlywriting.android.beta.activity.MainActivity
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentMonth
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentYear
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).setToolbarTitle(resources.getString(R.string.title_home))
        setRecyclerView()
        setObserver()
        setFunction()
    }

    private fun setRecyclerView() {
        binding.rvGoal.apply {
            adapter = MonthlyGoalAdapter { position -> openDetailWithId(position) }
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun openDetailWithId(position: Int) {
        val selectedId = viewModel.monthlyGoalListAsLiveData.value!![position].id
        val action = HomeFragmentDirections.openGoalDetail(selectedId, true)
        findNavController().navigate(action)
    }

    private fun setObserver() {
        viewModel.monthlyGoalListAsLiveData.observe(viewLifecycleOwner) {
            (binding.rvGoal.adapter as MonthlyGoalAdapter).differ.submitList(it)
            binding.tvGoalNum.text = resources.getString(R.string.text_goal_num, it.size)
        }
    }

    private fun setFunction() {
        binding.apply {
            ivAddGoal.setOnClickListener {
                it.findNavController().navigate(R.id.open_goal_add)
            }

            btnMonthlyWriting.setOnClickListener {
                val action = HomeFragmentDirections.openMonthlyWriting(currentYear, currentMonth)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}