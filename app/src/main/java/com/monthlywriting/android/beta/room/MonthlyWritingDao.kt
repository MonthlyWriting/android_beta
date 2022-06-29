package com.monthlywriting.android.beta.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.monthlywriting.android.beta.model.MonthlyWriting

@Dao
interface MonthlyWritingDao {

    //----------get----------//
    @Query("Select * From monthly_writing Where year = :year and month = :month")
    fun getByMonth(year: Int, month: Int): MonthlyWriting?

    //    @Query("Select * From monthly_writing Where year = :year")
//    fun getByYear(year: Int): List<MonthlyWriting>
//
    //----------insert----------//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(monthlyWriting: MonthlyWriting)

    //----------update----------//
//    @Query("Update monthly_writing Set memo = :memo WHERE id = :id")
//    fun updateMemo(id: String, memo: MutableList<DailyMemo>)
//
    @Query("Update monthly_writing Set photo_list = :photoList WHERE id = :id ")
    fun updatePhotoList(id: Int, photoList: MutableList<String>)

    @Query("Update monthly_writing Set rating = :rating WHERE id = :id ")
    fun updateRating(id: Int, rating: HashMap<String, Any>)

    @Query("Update monthly_writing Set writing = :writing WHERE id = :id ")
    fun updateWriting(id: Int, writing: String)

//    @Query("Update monthly_writing Set writing = :writing WHERE id = :id ")
//    fun updateWriting(id: String, writing: String)

}