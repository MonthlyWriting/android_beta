package com.monthlywriting.android.beta.ui.writing.shorts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WritingShortViewModel: ViewModel() {

    val textEmoji = MutableLiveData<String>()
    val numPercentage = MutableLiveData<String>()
    val numStars = MutableLiveData<Float>()

}