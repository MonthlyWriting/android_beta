package com.monthlywriting.android.beta.walkthrough.name

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NameViewModel : ViewModel() {

    val name = MutableLiveData<String>()
}