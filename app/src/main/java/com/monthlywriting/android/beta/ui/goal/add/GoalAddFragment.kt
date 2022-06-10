package com.monthlywriting.android.beta.ui.goal.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.adapter.TempGoalAdapter
import com.monthlywriting.android.beta.databinding.FragmentGoalAddBinding
import com.monthlywriting.android.beta.activity.MainActivity
import com.monthlywriting.android.beta.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalAddFragment : Fragment() {

    private var _binding: FragmentGoalAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GoalAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goal_add, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).setToolbarTitle(resources.getString(R.string.title_goal_add))
        setRecyclerView()
        setFunction()
    }

    private fun setRecyclerView() {
        binding.rvGoal.apply {
            adapter = TempGoalAdapter { position -> deleteItem(position) }
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.tempList.observe(viewLifecycleOwner) {
            (binding.rvGoal.adapter as TempGoalAdapter).differ.submitList(it)
        }
    }

    private fun deleteItem(position: Int) {
        viewModel.deleteFromTempList(position)
    }

    private fun setFunction() {
        binding.ivAddGoal.apply {
            setOnClickListener { addGoal() }
            isEnabled = false
        }

        viewModel.goal.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.ivAddGoal.apply {
                    setImageResource(R.drawable.ic_btn_check_disabled)
                    isEnabled = false
                }
            } else {
                binding.ivAddGoal.apply {
                    setImageResource(R.drawable.ic_btn_check_enabled)
                    isEnabled = true
                }
            }
        }

        (activity as MainActivity).toggleSaveBtn(true) {
            saveToDataBase()
            findNavController().navigateUp()
        }
    }

    private fun saveToDataBase() {
        val tempList = viewModel.tempList.value
        if (tempList != null) {
            for (goal in tempList) {
                viewModel.insertToDataBase(goal)
            }
        }
    }

    private fun addGoal() {
        val newGoal = viewModel.goal.value!!
        viewModel.addToTempList(newGoal)
        viewModel.goal.value = ""
        context?.hideKeyboard(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).toggleSaveBtn(false) {}
        _binding = null
    }
}