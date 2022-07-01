package com.monthlywriting.android.beta.walkthrough.authority

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.MainActivity
import com.monthlywriting.android.beta.activity.WritingActivity
import com.monthlywriting.android.beta.databinding.FragmentAuthorityBinding
import com.monthlywriting.android.beta.di.App
import com.monthlywriting.android.beta.util.CustomTypefaceSpan
import com.monthlywriting.android.beta.walkthrough.WalkThroughActivity

class AuthorityFragment : Fragment() {

    private var _binding: FragmentAuthorityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthorityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTextFont()

        binding.btnNotificationFalse.setOnClickListener {
            startMainActivity()
        }

        binding.btnNotificationTrue.setOnClickListener {
            startMainActivity()
        }
    }

    private fun setTextFont() {
        val font = CustomTypefaceSpan(Typeface.create(ResourcesCompat.getFont(
            requireContext(), R.font.font_pretendard_semibold), Typeface.NORMAL))

        binding.tvAuthorityChat.text =
            SpannableStringBuilder(resources.getString(R.string.text_authority_chat,
                App.prefs.namePref)).also {
                it.setSpan(font, 0, it.split("님!")[0].length, 0)
            }
    }

    private fun startMainActivity() {
        (activity as WalkThroughActivity).finish()
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        App.prefs.walkthroughPref = true
    }
}