package com.ismkr.schedio.utils

import com.ismkr.schedio.R
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val calendar: Calendar = Calendar.getInstance()
    
    private val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.FRANCE)
    private val simpleTimeFormat = SimpleDateFormat("HH:mm", Locale.FRANCE)

    val todayDate: String get() = simpleDateFormat.format(calendar.time)

    fun formatDate(date: Date) : String = simpleDateFormat.format(date.time)

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

    fun compareTime(time: String, duration: String): Int {
        val startTime = simpleTimeFormat.parse(time)
        val endTime = Date(startTime!!.time + simpleTimeFormat.parse(duration)!!.time)

        val currentTime = simpleTimeFormat.parse("${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}")

        if (currentTime!!.time >= startTime.time && currentTime.time < endTime.time) {
            return 1
        } else if (currentTime!!.time - endTime.time >= 0) {
            return -1
        }

        return 0
    }

    fun getTimeDifference(time: String, duration: String): String {
        val startTime = simpleTimeFormat.parse(time)
        val currentTime = simpleTimeFormat.parse("${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}")

        return when {
            currentTime!!.time - startTime!!.time >= 0 -> simpleTimeFormat.format(Date(currentTime!!.time - startTime!!.time))
            else                                       -> simpleTimeFormat.format(Date(startTime!!.time - currentTime!!.time))
        }
    }

}