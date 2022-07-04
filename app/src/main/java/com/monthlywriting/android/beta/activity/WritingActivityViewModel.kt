package com.monthlywriting.android.beta.activity

import android.util.Log
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

    private val writingPhotoList = mutableListOf<String>()

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

    fun saveAll() {
        viewModelScope.launch {
            val list = _monthlyGoalTempList.value!!
            list.forEachIndexed { index, monthlyGoal ->
                if (index == list.lastIndex) {
                    writingRepository.updateWriting(monthlyGoal.id, monthlyGoal.writing ?: "")
                } else {
                    goalRepository.updateWriting(monthlyGoal.id, monthlyGoal.writing ?: "")
                }
            }

            _monthlyGoalList.value = list
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
                writingPhotoList.add(it)
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

    fun insertPhoto(newPhotoPath: String) {
        viewModelScope.launch {
            val currentPhotoList = _photoList.value?.toMutableList() ?: mutableListOf()
            currentPhotoList.add(newPhotoPath)
            val l = _monthlyGoalList.value

            writingPhotoList.add(newPhotoPath)
            writingRepository.updatePhotoList(l!![l.lastIndex].id, writingPhotoList)
            _photoList.value = currentPhotoList
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