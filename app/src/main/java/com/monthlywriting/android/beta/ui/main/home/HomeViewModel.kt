package com.monthlywriting.android.beta.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.model.MonthlyWriting
import com.monthlywriting.android.beta.repository.GoalRepository
import com.monthlywriting.android.beta.repository.WritingRepository
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentMonth
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentYear
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val writingRepository: WritingRepository,
) : ViewModel() {

    val monthlyGoalListAsLiveData: LiveData<List<MonthlyGoal>> by lazy {
        goalRepository.getByMonthAsLiveData(currentYear, currentMonth)
    }

    private val _monthlyWriting = MutableLiveData<MonthlyWriting>()
    val monthlyWriting: LiveData<MonthlyWriting> get() = _monthlyWriting

}