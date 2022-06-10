package com.monthlywriting.android.beta.repository

import androidx.lifecycle.LiveData
import com.monthlywriting.android.beta.model.DailyMemo
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.room.MonthlyGoalDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoalRepository @Inject constructor(
    private val monthlyGoalDao: MonthlyGoalDao,
) {
    //----------get----------//
    fun getByMonthAsLiveData(year: Int, month: Int): LiveData<List<MonthlyGoal>> {
        return monthlyGoalDao.getByMonthAsLiveData(year, month)
    }

    suspend fun getByMonth(year: Int, month: Int): List<MonthlyGoal> = withContext(Dispatchers.IO) {
        return@withContext monthlyGoalDao.getByMonth(year, month)
    }

    suspend fun getById(id: Int): MonthlyGoal = withContext(Dispatchers.IO) {
        return@withContext monthlyGoalDao.getById(id)
    }

    //----------insert----------//
    suspend fun insert(monthlyGoal: MonthlyGoal) = withContext(Dispatchers.IO) {
        monthlyGoalDao.insert(monthlyGoal)
    }

    //----------update----------//
    suspend fun updateMemo(id: Int, memo: MutableList<DailyMemo>) = withContext(Dispatchers.IO) {
        return@withContext monthlyGoalDao.updateMemo(id, memo)
    }

    suspend fun updatePhotoList(id: Int, photoList: MutableList<String>) =
        withContext(Dispatchers.IO) {
            return@withContext monthlyGoalDao.updatePhotoList(id, photoList)
        }

    suspend fun updateRating(id: Int, rating: HashMap<String, Any>) =
        withContext(Dispatchers.IO) {
            return@withContext monthlyGoalDao.updateRating(id, rating)
        }

    suspend fun updateWriting(id: Int, writing: String) =
        withContext(Dispatchers.IO) {
            return@withContext monthlyGoalDao.updateWriting(id, writing)
        }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        return@withContext monthlyGoalDao.delete(id)
    }
}