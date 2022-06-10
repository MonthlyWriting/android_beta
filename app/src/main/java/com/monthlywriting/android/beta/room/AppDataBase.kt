package com.monthlywriting.android.beta.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.monthlywriting.android.beta.model.MonthlyGoal
import com.monthlywriting.android.beta.model.MonthlyWriting

@Database(
    entities = [MonthlyGoal::class, MonthlyWriting::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(RoomConverter::class)
abstract class AppDataBase: RoomDatabase() {
    abstract fun monthlyGoalDao(): MonthlyGoalDao
    abstract fun monthlyWritingDao(): MonthlyWritingDao
}