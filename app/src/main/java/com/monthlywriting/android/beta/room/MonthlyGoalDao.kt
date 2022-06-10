package com.monthlywriting.android.beta.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.monthlywriting.android.beta.model.DailyMemo
import com.monthlywriting.android.beta.model.MonthlyGoal

@Dao
interface MonthlyGoalDao {

    //----------get----------//
    @Query("Select * From monthly_goal Where year = :year and month = :month")
    fun getByMonthAsLiveData(year: Int, month: Int): LiveData<List<MonthlyGoal>>

    @Query("Select * From monthly_goal Where year = :year and month = :month")
    fun getByMonth(year: Int, month: Int): List<MonthlyGoal>

    @Query("Select * From monthly_goal Where id = :id")
    fun getById(id: Int): MonthlyGoal

    //----------insert----------//
    @Insert
    fun insert(monthlyGoal: MonthlyGoal)

//    //----------update----------//
//    @Query("Update monthly_goal Set writing = :writing WHERE id = :id ")
//    fun updateWriting(id: Int, writing: String)
//
    @Query("Update monthly_goal Set memo = :memo WHERE id = :id ")
    fun updateMemo(id: Int, memo: MutableList<DailyMemo>)

    @Query("Update monthly_goal Set photo_list = :photoList WHERE id = :id ")
    fun updatePhotoList(id: Int, photoList: MutableList<String>)

    @Query("Update monthly_goal Set rating = :rating WHERE id = :id ")
    fun updateRating(id: Int, rating: HashMap<String, Any>)

    @Query("Update monthly_goal Set writing = :writing WHERE id = :id ")
    fun updateWriting(id: Int, writing: String)

//    @Query("Update monthly_goal Set content = :content WHERE id = :id ")
//    fun updateGoal(id: Int, content: String)
//
    //----------delete----------//
    @Query("DELETE FROM monthly_goal WHERE id = :id")
    fun delete(id: Int)
}