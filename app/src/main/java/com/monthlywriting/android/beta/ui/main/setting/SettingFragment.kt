package com.monthlywriting.android.beta.ui.main.setting

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.MainActivity
import com.monthlywriting.android.beta.alarm.AlarmReceiver
import com.monthlywriting.android.beta.databinding.FragmentSettingBinding
import com.monthlywriting.android.beta.di.App
import com.monthlywriting.android.beta.util.setNotification

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).setToolbarTitle(resources.getString(R.string.title_setting))
        (activity as MainActivity).setFragmentMargin(false)
        setNotificationFunction()
    }

    private fun setNotificationFunction() {
        binding.switchNotification.isChecked = App.prefs.notificationPref

        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    App.prefs.notificationPref = true
                    setNotification(requireContext(), true)
                }
                false -> {
                    App.prefs.notificationPref = false
                    setNotification(requireContext(), false)
                }
            }
        }

        binding.textSurvey.setOnClickListener {

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).setFragmentMargin(true)
        _binding = null
    }
}