package com.monthlywriting.android.beta.walkthrough.name

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.FragmentNameBinding
import com.monthlywriting.android.beta.di.App


class NameFragment : Fragment() {

    private var _binding: FragmentNameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_name, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        saveName()
        setFunction()
    }

    private fun saveName() {
        App.prefs.namePref = viewModel.name.value
    }

    private fun setFunction() {
        binding.btnNext.setOnClickListener {
            if (viewModel.name.value.isNullOrBlank()) {
                Toast.makeText(requireContext(), resources.getString(R.string.toast_no_name),
                    Toast.LENGTH_SHORT).show()
            } else {
                App.prefs.namePref = viewModel.name.value
                it.findNavController().navigate(R.id.move_to_authority)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}