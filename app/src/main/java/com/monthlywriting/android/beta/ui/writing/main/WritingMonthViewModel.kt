package com.monthlywriting.android.beta.ui.writing.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monthlywriting.android.beta.model.MonthlyWriting
import com.monthlywriting.android.beta.repository.WritingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WritingMonthViewModel @Inject constructor(
    private val repository: WritingRepository,
) : ViewModel() {

    val writing = MutableLiveData<String>()

    fun saveWriting(
        year: Int,
        month: Int,
        writing: String,
        photoList: MutableList<String>,
    ) {
        viewModelScope.launch {
            repository.insert(
                MonthlyWriting(
                    id = 0,
                    year = year,
                    month = month,
                    writing = writing,
                    rating = hashMapOf("emoji" to ""),
                    photoList = photoList
                )
            )
        }
    }

    fun getWriting(
        year: Int,
        month: Int,
    ) {
        viewModelScope.launch {
            writing.value = repository.getByMonth(year, month)?.writing ?: ""
        }
    }

}