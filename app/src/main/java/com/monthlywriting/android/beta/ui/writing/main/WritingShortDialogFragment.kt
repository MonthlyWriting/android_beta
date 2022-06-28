package com.monthlywriting.android.beta.ui.writing.main

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.WritingActivityViewModel
import com.monthlywriting.android.beta.databinding.FragmentWritingShortDialogBinding
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.ui.writing.shorts.WritingShortViewModel
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WritingShortDialogFragment : DialogFragment() {

    private var _binding: FragmentWritingShortDialogBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel: WritingActivityViewModel by activityViewModels()
    private val viewModel: WritingShortViewModel by viewModels()

    private lateinit var currentGoal: MonthlyGoal
    private var checkedType = "emoji"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater,
                R.layout.fragment_writing_short_dialog,
                container,
                false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentGoal = activityViewModel.selectedGoal.value!!

        setRadioFunction()
        setEmojiFunction()
        setPercentageFunction()
        setRatingbarFunction()
        setLevelFunction()
        setInitialData()
        setFunction()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.transparent_dialog)
        dialog.apply {
            setCanceledOnTouchOutside(true)
        }
        return dialog
    }

    override fun onResume() {
        super.onResume()

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val x = (size.x * 0.95).toInt()

            this.dialog?.window?.setLayout(x, -2)

        } else {
            val rect = windowManager.currentWindowMetrics.bounds
            val x = (rect.width() * 0.95).toInt()

            this.dialog?.window?.setLayout(x, -2)
        }
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

    private fun setFunction() {
        binding.apply {
            ivSave.setOnClickListener {
                saveRating()
                dismiss()
                // recyclerview refresh 하기
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
            val rating = hashMapOf(checkedType to evaluation)

            activityViewModel.insertRating(currentGoal.id, rating)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}