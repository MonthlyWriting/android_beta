package com.monthlywriting.android.beta.ui.main.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.repository.GoalRepository
import com.monthlywriting.android.beta.repository.WritingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val writingRepository: WritingRepository,
) : ViewModel() {

    private val _isNullList = MutableLiveData<List<Boolean>>()
    val isNullList: LiveData<List<Boolean>> get() = _isNullList

    private val _monthlyGoalList = MutableLiveData<List<MonthlyGoal>>()
    val monthlyGoalList: LiveData<List<MonthlyGoal>> get() = _monthlyGoalList

    fun getIsNullList(year: Int) {
        viewModelScope.launch {
            val list = mutableListOf<Boolean>()
            for (month in 1..12) {
                if (goalRepository.getByMonth(year, month).isEmpty()
                    && writingRepository.getByMonth(year, month) == null
                ) {
                    list.add(true)
                } else {
                    list.add(false)
                }
            }

            _isNullList.value = list
        }
    }

    fun getMonthlyGoalList(year: Int, month: Int) {
        viewModelScope.launch {
            _monthlyGoalList.value = goalRepository.getByMonth(year, month)
        }
    }
}