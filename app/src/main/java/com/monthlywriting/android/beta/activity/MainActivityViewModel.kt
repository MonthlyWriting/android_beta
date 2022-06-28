package com.monthlywriting.android.beta.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monthlywriting.android.beta.model.MonthlyWriting
import com.monthlywriting.android.beta.repository.WritingRepository
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentMonth
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentYear
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val writingRepository: WritingRepository,
) : ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    fun setTitle(title: String) {
        _title.value = title
    }

    fun insertWriting() {
        viewModelScope.launch {
            if (writingRepository.getByMonth(currentYear, currentMonth) == null) {
                writingRepository.insert(
                    MonthlyWriting(
                        id = 0,
                        year = currentYear,
                        month = currentMonth,
                        writing = "",
                        rating = hashMapOf("emoji" to ""),
                        photoList = mutableListOf()
                    )
                )
            }
        }
    }
}