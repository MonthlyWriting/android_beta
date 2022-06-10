package com.monthlywriting.android.beta.ui.writing.shorts

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.WritingActivityViewModel
import com.monthlywriting.android.beta.databinding.FragmentWritingShortBinding
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.util.CustomTypefaceSpan
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WritingShortFragment : Fragment() {

    private var _binding: FragmentWritingShortBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel: WritingActivityViewModel by activityViewModels()
    private val viewModel: WritingShortViewModel by viewModels()

    private lateinit var currentGoal: MonthlyGoal
    private var checkedType = "emoji"
    private var isSet: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_writing_short, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentGoal = activityViewModel.selectedGoal.value!!

        setChatText()
        setRadioFunction()
        setEmojiFunction()
        setPercentageFunction()
        setRatingbarFunction()
        setLevelFunction()
        setInitialData()
        setVisibility()
        setFunction()
    }

    private fun setChatText() {
        val font = CustomTypefaceSpan(Typeface.create(ResourcesCompat.getFont(
            requireContext(), R.font.font_pretendard_semibold), Typeface.NORMAL))

        binding.tvChatShort1.text =
            SpannableStringBuilder(resources.getString(R.string.text_monthly_writing_chat_short_1,
                currentGoal.goal)).also { it.setSpan(font, 0, it.lines()[0].length, 0) }

        binding.tvChatShort2.text =
            SpannableStringBuilder(resources.getString(R.string.text_monthly_writing_chat_short_2,
                currentGoal.goal)).also { it.setSpan(font, 0, it.split("는\n")[0].length, 0) }
    }

    private fun setRadioFunction() {
        binding.rgType.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_emoji_rating -> {
                    showRatingType(0)
                    checkedType = "emoji"
                }

                R.id.rb_percentage_rating -> {
                    showRatingType(1)
                    checkedType = "percentage"
                }

                R.id.rb_star_rating -> {
                    showRatingType(2)
                    checkedType = "star"
                }

                R.id.rb_level_rating -> {
                    showRatingType(3)
                    checkedType = "level"
                }
            }
        }
    }

    private fun setEmojiFunction() {
        val keyBoard =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val popUp = EmojiPopup.Builder.fromRootView(binding.root)
            .setOnEmojiClickListener { _, _ ->
                keyBoard.hideSoftInputFromWindow(view?.windowToken, 0)
                binding.etEmojiRating.clearFocus()
            }
            .build(binding.etEmojiRating)

        binding.etEmojiRating.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                keyBoard.showSoftInput(this.view, 0)
                popUp.toggle()
                view.clearFocus()
            }
        }

        binding.etEmojiRating.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {
                val newEmoji = p0?.subSequence(start, start + after).toString()
                binding.etEmojiRating.text?.clear()
                viewModel.textEmoji.value = newEmoji
            }
        })
    }

    private fun setPercentageFunction() {
        viewModel.numPercentage.value = "0%"
        binding.seekbarPercentageRating.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.numPercentage.value = "${p0?.progress}%"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun setRatingbarFunction() {
        viewModel.numStars.value = (3).toFloat()
        binding.ratingbarStarRating.setOnRatingBarChangeListener { _, fl, _ ->
            viewModel.numStars.value = fl
        }
    }

    private fun setLevelFunction() {
        binding.layoutLevelRating.check(R.id.rb_good)
    }

    private fun showRatingType(position: Int) {
        listOf(
            binding.layoutEmojiRating,
            binding.layoutPercentageRating,
            binding.layoutStarRating,
            binding.layoutLevelRating
        ).forEachIndexed { index, view ->
            if (index == position) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }

        when (position) {
            0 -> {
                binding.rgType.check(R.id.rb_emoji_rating)
            }
            1 -> {
                binding.rgType.check(R.id.rb_percentage_rating)
            }
            2 -> {
                binding.rgType.check(R.id.rb_star_rating)
            }
            3 -> {
                binding.rgType.check(R.id.rb_level_rating)
            }
        }
    }

    private fun setInitialData() {
        val rating = currentGoal.rating
        val value = rating.getValue(rating.keys.first())
        checkedType = rating.keys.first()
        if (value != "") {
            isSet = true
        }

        binding.tvEvaluation.text = value.toString()
        binding.tvGoal.text = currentGoal.goal

        when (rating.keys.first()) {
            "emoji" -> {
                checkedType = "emoji"
                showRatingType(0)
                viewModel.textEmoji.value = value as String
            }
            "percentage" -> {
                checkedType = "percentage"
                showRatingType(1)
                viewModel.numPercentage.value = value as String
                binding.seekbarPercentageRating.progress = value.split("%")[0].toInt()
            }
            "star" -> {
                checkedType = "star"
                showRatingType(2)
                when (value) {
                    is Double -> {
                        binding.ratingbarStarRating.rating = value.toFloat()
                        viewModel.numStars.value = value.toFloat()
                    }
                    is Float -> {
                        binding.ratingbarStarRating.rating = value
                        binding.ratingbarStarRating.rating = value
                    }
                }

            }
            "level" -> {
                checkedType = "level"
                showRatingType(3)
                when (value as String) {
                    "상" -> {
                        binding.layoutLevelRating.check(R.id.rb_good)
                    }
                    "증" -> {
                        binding.layoutLevelRating.check(R.id.rb_fair)
                    }
                    "하" -> {
                        binding.layoutLevelRating.check(R.id.rb_poor)
                    }
                }
            }
        }
    }

    private fun setVisibility() {
        binding.apply {

            listOf(
                tvChatShort1,
                tvChatShort2,
                tvChatShort3,
                rgType,
                rlRating,
                rlSavedRating,
                btnSaveRating,
                btnStopMonthlyWriting,
                btnContinueMonthlyWriting
            ).forEach {
                it.visibility = View.GONE
            }

            if (isSet) {
                setVisibleAnimation(
                    listOf(
                        tvChatShort1,
                        rgType,
                        rlRating,
                        btnSaveRating,
                        tvChatShort2,
                        rlSavedRating,
                        tvChatShort3,
                        btnContinueMonthlyWriting,
                        btnStopMonthlyWriting))
            } else {
                setVisibleAnimation(
                    listOf(
                        tvChatShort1,
                        rgType,
                        rlRating,
                        btnSaveRating))
            }
        }
    }

    private fun setFunction() {
        binding.apply {
            btnSaveRating.setOnClickListener {
                saveRating()
            }

            btnStopMonthlyWriting.setOnClickListener {
                it.findNavController().navigateUp()
            }

            btnContinueMonthlyWriting.setOnClickListener {
                it.findNavController().navigate(R.id.open_writing_long)
            }

            root.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (isSet) {
                            listOf(
                                tvChatShort1,
                                rgType,
                                rlRating,
                                btnSaveRating,
                                tvChatShort2,
                                rlSavedRating,
                                tvChatShort3,
                                btnContinueMonthlyWriting,
                                btnStopMonthlyWriting).forEach {
                                it.clearAnimation()
                                it.visibility = View.VISIBLE
                            }
                        } else {
                            listOf(
                                tvChatShort1,
                                rgType,
                                rlRating,
                                btnSaveRating).forEach {
                                it.clearAnimation()
                                it.visibility = View.VISIBLE
                            }
                        }

                    }
                }

                view.performClick()
                false
            }
        }
    }

    private fun saveRating() {
        var evaluation: Any = ""

        when (checkedType) {
            "emoji" -> {
                evaluation = viewModel.textEmoji.value ?: "no value"
            }
            "percentage" -> {
                evaluation = viewModel.numPercentage.value ?: "no value"
            }
            "star" -> {
                evaluation = viewModel.numStars.value ?: "no value"
            }
            "level" ->
                when (binding.layoutLevelRating.checkedRadioButtonId) {
                    R.id.rb_good -> evaluation = "상"
                    R.id.rb_fair -> evaluation = "중"
                    R.id.rb_poor -> evaluation = "하"
                }
        }

        if (evaluation == "no value") {
            Toast.makeText(requireContext(), R.string.toast_no_retrospect_input, Toast.LENGTH_SHORT)
                .show()
        } else {
            if (!isSet) {
                setVisibleAnimation(
                    listOf(binding.tvChatShort2,
                        binding.rlSavedRating,
                        binding.tvChatShort3,
                        binding.btnContinueMonthlyWriting,
                        binding.btnStopMonthlyWriting))
                isSet = true
            }

            val rating = hashMapOf(checkedType to evaluation)
            activityViewModel.insertRating(currentGoal.id, rating)
        }

        binding.tvEvaluation.text = evaluation.toString()
    }

    private fun setVisibleAnimation(list: List<View>) {
        var time: Long = 0
        list.forEach {
            Handler(Looper.getMainLooper()).postDelayed({
                it.visibility = View.VISIBLE
                it.animate()
                    .alpha(1.0f)
                    .duration = 800
            }, time)
            time += 400
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}