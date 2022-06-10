package com.monthlywriting.android.beta.ui.goal.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monthlywriting.android.beta.model.DailyMemo
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.repository.GoalRepository
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentMonth
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentYear
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalAddViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
) : ViewModel() {

    val goal = MutableLiveData<String>()

    private val _tempList = MutableLiveData<List<String>>()
    val tempList: LiveData<List<String>> get() = _tempList

    fun addToTempList(newGoal: String) {
        val list = _tempList.value?.toMutableList() ?: mutableListOf()
        list.add(newGoal)

        _tempList.value = list
    }

    fun deleteFromTempList(position: Int) {
        val list = _tempList.value?.toMutableList() ?: mutableListOf()
        list.removeAt(position)

        _tempList.value = list
    }

    fun insertToDataBase(newGoal: String) {
        viewModelScope.launch {
            goalRepository.insert(monthlyGoal = MonthlyGoal(
                id = 0,
                year = currentYear,
                month = currentMonth,
                goal = newGoal,
                memo = mutableListOf(DailyMemo(System.currentTimeMillis(), "오늘 목표가 만들어졌어요! :)")),
                photoList = mutableListOf(),
                rating = hashMapOf("emoji" to ""),
                writing = null
            ))
        }
    }
}