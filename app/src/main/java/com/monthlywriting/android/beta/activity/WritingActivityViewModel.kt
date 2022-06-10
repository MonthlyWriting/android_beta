package com.monthlywriting.android.beta.activity

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

    private val _monthlyWriting = MutableLiveData<MonthlyWriting>()
    val monthlyWriting: LiveData<MonthlyWriting> get() = _monthlyWriting

    private val _monthlyPhotoList = MutableLiveData<List<String>>()
    val monthlyPhotoList: LiveData<List<String>> get() = _monthlyPhotoList

    private val _allPhotoList = MutableLiveData<List<String>>()
    val allPhotoList: LiveData<List<String>> get() = _allPhotoList

    private val _selectedGoal = MutableLiveData<MonthlyGoal>()
    val selectedGoal: LiveData<MonthlyGoal> get() = _selectedGoal

    private val _selectedGoalPhotoList = MutableLiveData<List<String>>()
    val selectedGoalPhotoList: LiveData<List<String>> get() = _selectedGoalPhotoList

    var selectedIndex = -1

    fun selectGoal(index: Int) {
        _selectedGoal.value = _monthlyGoalList.value!![index]
        _selectedGoalPhotoList.value = _monthlyGoalList.value!![index].photoList
        selectedIndex = index
    }

    fun getMonthlyGoalList(year: Int, month: Int) {
        viewModelScope.launch {
            _monthlyGoalList.value = goalRepository.getByMonth(year, month)
        }
    }

    fun getMonthlyWriting(year: Int, month: Int) {
        viewModelScope.launch {
            val currentMonthlyWriting = writingRepository.getByMonth(year, month)
            _monthlyWriting.value = currentMonthlyWriting
            _monthlyPhotoList.value = currentMonthlyWriting?.photoList
        }
    }

    fun getAllPhotoList(year: Int, month: Int) {
        viewModelScope.launch {
            val list = mutableListOf<String>()

            goalRepository.getByMonth(year, month).forEach {
                it.photoList.forEach { each ->
                    list.add(each)
                }
            }

            writingRepository.getByMonth(year, month)?.photoList?.forEach {
                list.add(it)
            }

            _allPhotoList.value = list
        }
    }

    fun insertGoalPhoto(newPhotoPath: String) {
        viewModelScope.launch {
            val currentPhotoList = _selectedGoalPhotoList.value?.toMutableList() ?: mutableListOf()
            currentPhotoList.add(newPhotoPath)

            goalRepository.updatePhotoList(_selectedGoal.value?.id!!, currentPhotoList)
            _selectedGoalPhotoList.value = currentPhotoList
        }
    }

    fun insertPhoto(newPhotoPath: String) {
        viewModelScope.launch {
            val currentPhotoList = _monthlyPhotoList.value?.toMutableList() ?: mutableListOf()
            currentPhotoList.add(newPhotoPath)

            writingRepository.updatePhotoList(_monthlyWriting.value?.id!!, currentPhotoList)
            _monthlyPhotoList.value = currentPhotoList
        }
    }

    fun insertRating(id: Int, rating: HashMap<String, Any>) {
        viewModelScope.launch {
            val list = _monthlyGoalList.value!!
            list[selectedIndex].rating = rating

            goalRepository.updateRating(id, rating)
            _monthlyGoalList.value = list

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