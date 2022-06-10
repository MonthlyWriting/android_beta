package com.monthlywriting.android.beta.util

import android.content.Context
import java.time.LocalDate
import java.util.*

class CurrentInfo {

    companion object {
        private val currentTime: LocalDate = LocalDate.now()

        val currentYear: Int = currentTime.year
        val currentMonth: Int = currentTime.monthValue
        val currentDate: Int = currentTime.dayOfMonth

        fun getCurrentEndDateOfMonth(): Int {
            val cal = Calendar.getInstance()
            cal.set(currentYear, currentMonth - 1, currentDate)
            return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
    }
}