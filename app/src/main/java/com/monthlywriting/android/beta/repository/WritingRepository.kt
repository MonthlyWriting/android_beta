package com.monthlywriting.android.beta.repository

import androidx.lifecycle.LiveData
import com.monthlywriting.android.beta.model.MonthlyWriting
import com.monthlywriting.android.beta.room.MonthlyWritingDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WritingRepository @Inject constructor(
    private val monthlyWritingDao: MonthlyWritingDao,
) {

    //----------get----------//
    suspend fun getByMonth(year: Int, month: Int): MonthlyWriting? = withContext(Dispatchers.IO) {
        return@withContext monthlyWritingDao.getByMonth(year, month)
    }

    //    suspend fun getByYear(year: Int): List<MonthlyWriting> = withContext(Dispatchers.IO) {
//        return@withContext monthlyWritingDao.getByYear(year)
//    }
//
    //----------insert----------//
    suspend fun insert(monthlyWriting: MonthlyWriting) = withContext(Dispatchers.IO) {
        monthlyWritingDao.insert(monthlyWriting)
    }

    //----------update----------//
    suspend fun updatePhotoList(id: Int, photoList: MutableList<String>) =
        withContext(Dispatchers.IO) {
            return@withContext monthlyWritingDao.updatePhotoList(id, photoList)
        }

    suspend fun updateRating(id: Int, rating: HashMap<String, Any>) =
        withContext(Dispatchers.IO) {
            return@withContext monthlyWritingDao.updateRating(id, rating)
        }

    suspend fun updateWriting(id: Int, writing: String) =
        withContext(Dispatchers.IO) {
            return@withContext monthlyWritingDao.updateWriting(id, writing)
        }

//    suspend fun updateWriting(id: String, writing: String) = withContext(Dispatchers.IO) {
//        return@withContext monthlyWritingDao.updateWriting(id, writing)
//    }


}