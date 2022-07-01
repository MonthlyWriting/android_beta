package com.monthlywriting.android.beta.ui.writing.closing

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.FragmentClosingLoadingBinding
import java.util.*

class ClosingLoadingFragment : Fragment() {

    private var _binding: FragmentClosingLoadingBinding? = null
    private val binding get() = _binding!!

    private lateinit var timer: Timer
    private var i = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentClosingLoadingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTimerFunction()
        navigate()
    }

    private fun setTimerFunction() {
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    when (i) {
                        0 -> {
                            binding.tvLoading.text =
                                resources.getStringArray(R.array.text_closing_loading)[1].toString()
                            i = 1
                        }
                        1 -> {
                            binding.tvLoading.text =
                                resources.getStringArray(R.array.text_closing_loading)[2].toString()
                            i = 2
                        }
                        2 -> {
                            binding.tvLoading.text =
                                resources.getStringArray(R.array.text_closing_loading)[0].toString()
                            i = 0
                        }
                    }
                }
            }
        }, 0, 300)
    }

    private fun navigate() {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.open_closing_paper)
        }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        timer.cancel()
    }
}