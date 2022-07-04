package com.monthlywriting.android.beta.ui.guide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.adapter.GuideAdapter
import com.monthlywriting.android.beta.databinding.FragmentGuideContainerBinding
import com.monthlywriting.android.beta.di.App

class GuideContainerFragment : Fragment() {

    private var _binding: FragmentGuideContainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGuideContainerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewPager()
    }

    private fun setViewPager() {
        binding.vpGuide.apply {
            adapter = GuideAdapter()
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.tvPage.text =
                        resources.getString(R.string.text_guide_page_num, position + 1)
                    when (position) {
                        0 -> {
                            binding.btnNext.text = getString(R.string.text_guide_next)
                            binding.btnBack.visibility = View.GONE
                        }
                        1 -> {
                            binding.btnNext.text = getString(R.string.text_guide_next)
                            binding.btnBack.visibility = View.VISIBLE
                        }
                        2 -> {
                            binding.btnNext.text = getString(R.string.text_guide_start)
                            binding.btnBack.visibility = View.VISIBLE
                        }

                    }

                    binding.btnBack.setOnClickListener {
                        binding.vpGuide.setCurrentItem(position - 1, true)
                    }

                    binding.btnNext.setOnClickListener {
                        if (position == 2) {
                            closeGuide()
                            App.prefs.guidePref = true
                        } else {
                            binding.vpGuide.setCurrentItem(position + 1, true)
                        }
                    }
                }
            })
        }
    }

    private fun closeGuide() {
        requireActivity().supportFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}