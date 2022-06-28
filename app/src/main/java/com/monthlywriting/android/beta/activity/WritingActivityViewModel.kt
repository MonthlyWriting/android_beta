package com.monthlywriting.android.beta.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.model.MonthlyWriting
import com.monthlywriting.android.beta.repository.GoalRepository
import com.monthlywriting.android.beta.repository.WritingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WritingActivityViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val writingRepository: WritingRepository,
) : ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    fun setTitle(title: String) {
        _title.value = title
    }

    private val _monthlyGoalList = MutableLiveData<List<MonthlyGoal>>()
    val monthlyGoalList: LiveData<List<MonthlyGoal>> get() = _monthlyGoalList

    private val _monthlyGoalTempList = MutableLiveData<List<MonthlyGoal>>()
    val monthlyGoalTempList: LiveData<List<MonthlyGoal>> get() = _monthlyGoalTempList

    private val _photoList = MutableLiveData<List<String>>()
    val photoList: LiveData<List<String>> get() = _photoList

    private val _selectedGoal = MutableLiveData<MonthlyGoal>()
    val selectedGoal: LiveData<MonthlyGoal> get() = _selectedGoal

    var selectedIndex = -1

    fun getMonthlyGoalList(year: Int, month: Int) {
        viewModelScope.launch {
            val list = goalRepository.getByMonth(year, month).toMutableList()
            val monthlyWriting = writingRepository.getByMonth(year, month)
            val monthlyWritingAsMonthlyGoal = MonthlyGoal(
                monthlyWriting!!.id,
                monthlyWriting.year,
                monthlyWriting.month,
                "한 달 돌아보기",
                mutableListOf(),
                monthlyWriting.photoList,
                monthlyWriting.rating,
                monthlyWriting.writing
            )

            list.add(monthlyWritingAsMonthlyGoal)
            _monthlyGoalList.value = list
        }
    }

    fun saveTempWriting(index: Int, writing: String) {
        val list = _monthlyGoalList.value!!
        list[index].writing = writing
        _monthlyGoalTempList.value = list
    }

    fun saveWriting() {
        if (_monthlyGoalTempList.value != null) {
            _monthlyGoalList.value = _monthlyGoalTempList.value
            Log.d("why", _monthlyGoalList.value.toString())
        }
    }

    fun getPhotoList(year: Int, month: Int) {
        viewModelScope.launch {
            val photoList = mutableListOf<String>()

            goalRepository.getByMonth(year, month).forEach {
                it.photoList.forEach { each ->
                    photoList.add(each)
                }
            }

            writingRepository.getByMonth(year, month)?.photoList?.forEach {
                photoList.add(it)
            }

            _photoList.value = photoList
        }
    }

    fun getSelectedGoal(year: Int, month: Int) {
        viewModelScope.launch {
            if (selectedIndex == -1) {
                val monthlyWriting = writingRepository.getByMonth(year, month)
                val monthlyWritingAsMonthlyGoal = MonthlyGoal(
                    monthlyWriting!!.id,
                    monthlyWriting.year,
                    monthlyWriting.month,
                    "한 달 돌아보기",
                    mutableListOf(),
                    monthlyWriting.photoList,
                    monthlyWriting.rating,
                    monthlyWriting.writing
                )

                _selectedGoal.value = monthlyWritingAsMonthlyGoal
            } else {
                _selectedGoal.value = _monthlyGoalList.value!![selectedIndex]
            }
        }
    }


    fun insertGoalPhoto(newPhotoPath: String) {
        viewModelScope.launch {
//            val currentPhotoList = _selectedGoalPhotoList.value?.toMutableList() ?: mutableListOf()
//            currentPhotoList.add(newPhotoPath)
//
//            goalRepository.updatePhotoList(_selectedGoal.value?.id!!, currentPhotoList)
//            _selectedGoalPhotoList.value = currentPhotoList
        }
    }

    fun insertPhoto(newPhotoPath: String) {
        viewModelScope.launch {
//            val currentPhotoList = _monthlyPhotoList.value?.toMutableList() ?: mutableListOf()
//            currentPhotoList.add(newPhotoPath)
//
//            writingRepository.updatePhotoList(_monthlyWriting.value?.id!!, currentPhotoList)
//            _monthlyPhotoList.value = currentPhotoList
        }
    }

    fun insertRating(id: Int, rating: HashMap<String, Any>) {
        viewModelScope.launch {
            if (selectedIndex == -1) {
                val list = _monthlyGoalList.value!!
                list[list.lastIndex].rating = rating

                writingRepository.updateRating(id, rating)
                _monthlyGoalList.value = list
            } else {
                val list = _monthlyGoalList.value!!
                list[selectedIndex].rating = rating

                goalRepository.updateRating(id, rating)
                _monthlyGoalList.value = list
            }
        }
    }

    fun insertWriting(id: Int, writing: String) {
        viewModelScope.launch {
            val list = _monthlyGoalList.value!!
            list[selectedIndex].writing = writing

            goalRepository.updateWriting(id, writing)
            _monthlyGoalList.value = list
        }
    }
}