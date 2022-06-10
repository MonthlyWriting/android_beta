package com.monthlywriting.android.beta.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.monthlywriting.android.beta.model.DailyMemo
import com.monthlywriting.android.beta.model.MonthlyGoal

class RoomConverter {

    @TypeConverter
    fun monthlyGoalListToJson(list: List<MonthlyGoal>): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToMonthlyGoalList(json: String): List<MonthlyGoal> {
        return Gson().fromJson(json, Array<MonthlyGoal>::class.java).toList()
    }

    @TypeConverter
    fun dailyMemoListToJson(list: List<DailyMemo>): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToDailyMemoList(json: String): List<DailyMemo> {
        return Gson().fromJson(json, Array<DailyMemo>::class.java).toMutableList()
    }

    @TypeConverter
    fun stringMutableListToJson(list: List<String>): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToStringMutableList(json: String): List<String> {
        return Gson().fromJson(json, Array<String>::class.java).toMutableList()
    }

    @TypeConverter
    fun ratingToJson(map: HashMap<String, Any>): String? {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun jsonToRating(json: String): HashMap<String, Any> {
        return Gson().fromJson(json, HashMap<String, Any>()::class.java)
    }
}