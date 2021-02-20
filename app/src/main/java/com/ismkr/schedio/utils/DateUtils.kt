package com.ismkr.schedio.utils

import com.ismkr.schedio.R
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    val calendar = Calendar.getInstance()

    val todayDate: String get() {
        val today = calendar.time
        val pattern = "MM/dd/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)

        return simpleDateFormat.format(today)
    }

    fun getYear(): Int {
        return calendar.get(Calendar.YEAR)
    }

    fun getMonth(): Int {
        return calendar.get(Calendar.MONTH)
    }

    fun getDayOfMonth(): Int {
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getMonthResId(month: Int): Int = when (month) {
        0 -> R.string.month_01
        1 -> R.string.month_02
        2 -> R.string.month_03
        3 -> R.string.month_04
        4 -> R.string.month_05
        5 -> R.string.month_06
        6 -> R.string.month_07
        7 -> R.string.month_08
        8 -> R.string.month_09
        9 -> R.string.month_10
        10 -> R.string.month_11
        11 -> R.string.month_12
        else -> throw Exception("Month Not Valid!")
    }

    fun getThisMonthResId(): Int = getMonthResId(calendar.get(Calendar.MONTH))

}