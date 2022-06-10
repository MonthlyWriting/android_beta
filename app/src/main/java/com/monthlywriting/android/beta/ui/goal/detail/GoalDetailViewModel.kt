package com.monthlywriting.android.beta.ui.goal.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monthlywriting.android.beta.model.DailyMemo
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalDetailViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
) : ViewModel() {

    private val _monthlyGoal = MutableLiveData<MonthlyGoal>()
    val monthlyGoal: LiveData<MonthlyGoal> get() = _monthlyGoal

    private val _photoList = MutableLiveData<List<String>>()
    val photoList: LiveData<List<String>> get() = _photoList

    private val _memo = MutableLiveData<List<DailyMemo>>()
    val memo: LiveData<List<DailyMemo>> get() = _memo

    fun getCurrentMonthlyGoal(id: Int) {
        viewModelScope.launch {
            val currentMonthlyGoal = goalRepository.getById(id)
            _monthlyGoal.value = currentMonthlyGoal
            _photoList.value = currentMonthlyGoal.photoList
            _memo.value = currentMonthlyGoal.memo
        }
    }

    fun insertPhoto(newPhotoPath: String) {
        viewModelScope.launch {
            val currentPhotoList = _photoList.value?.toMutableList() ?: mutableListOf()
            currentPhotoList.add(newPhotoPath)

            goalRepository.updatePhotoList(_monthlyGoal.value?.id!!, currentPhotoList)
            _photoList.value = currentPhotoList
        }
    }

    fun insertMemo(newMemo: DailyMemo) {
        viewModelScope.launch {
            val currentMemo = _memo.value?.toMutableList() ?: mutableListOf()
            currentMemo.add(newMemo)

            goalRepository.updateMemo(_monthlyGoal.value?.id!!, currentMemo)
            _memo.value = currentMemo
        }
    }

    fun updateMemo(index: Int, newMemo: DailyMemo) {
        viewModelScope.launch {
            val currentMemo = _memo.value?.toMutableList() ?: mutableListOf()
            currentMemo[index] = newMemo

            goalRepository.updateMemo(_monthlyGoal.value?.id!!, currentMemo)
            _memo.value = currentMemo
        }
    }

    fun deleteGoal(id: Int) {
        viewModelScope.launch {
            goalRepository.delete(id)
        }
    }

    fun deleteMemo(index: Int) {
        viewModelScope.launch {
            val currentMemo = _memo.value?.toMutableList() ?: mutableListOf()
            currentMemo.removeAt(index)

            goalRepository.updateMemo(_monthlyGoal.value?.id!!, currentMemo)
            _memo.value = currentMemo
        }
    }
}