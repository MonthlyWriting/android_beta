package com.monthlywriting.android.beta.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_goal")
data class MonthlyGoal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "goal") var goal: String,
    @ColumnInfo(name = "memo") var memo: MutableList<DailyMemo>,
    @ColumnInfo(name = "photo_list") var photoList: MutableList<String>,
    @ColumnInfo(name = "rating") var rating: HashMap<String, Any>,
    @ColumnInfo(name = "writing") var writing: String?,
)