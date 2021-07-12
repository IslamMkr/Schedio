package com.ismkr.schedio.utils

import android.content.Context
import com.ismkr.schedio.R
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {

    private val calendar: Calendar = Calendar.getInstance()
    
    private val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.FRANCE)
    private val simpleTimeFormat = SimpleDateFormat("HH:mm", Locale.FRANCE)

    val time: Date get() = calendar.time
    val todayDate: String get() = simpleDateFormat.format(calendar.time)
    val currentTime : String get() = simpleTimeFormat.format(calendar.time)

    fun timeAsHM(date: Date) : String = simpleTimeFormat.format(date.time)

    fun dateAsString(date: Date) : String = simpleDateFormat.format(date.time)

    fun dateAsMDYFormat(day: Int, month: Int, year: Int) = "${month+1}/$day/$year"

    fun fromUIFormatToMDYFormat(context: Context, uiDateFormat: String): String {
        val parts = uiDateFormat.split(", ")
        val parts02 = parts[0].split(" ")
        val year = parts[1].toInt()
        val month = parts02[0]
        val dayOfMonth = parts02[1].toInt()

        val cal = Calendar.getInstance()
        cal.set(Calendar.MONTH, getMonthNumber(context, month) - 1)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        cal.set(Calendar.YEAR, year)

        return simpleDateFormat.format(cal.time)
    }

    private fun getMonthNumber(context: Context, month: String) = when (month) {
        context.getString(R.string.month_01) -> 1
        context.getString(R.string.month_02) -> 2
        context.getString(R.string.month_03) -> 3
        context.getString(R.string.month_04) -> 4
        context.getString(R.string.month_05) -> 5
        context.getString(R.string.month_06) -> 6
        context.getString(R.string.month_07) -> 7
        context.getString(R.string.month_08) -> 8
        context.getString(R.string.month_09) -> 9
        context.getString(R.string.month_10) -> 10
        context.getString(R.string.month_11) -> 11
        context.getString(R.string.month_12) -> 12
        else -> throw Exception("Month Not Valid!")
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

    fun compareTime(time: String, duration: String): Int {
        val startTime = simpleTimeFormat.parse(time)
        val endTime = Date(startTime!!.time + simpleTimeFormat.parse(duration)!!.time)

        val currentTime = simpleTimeFormat.parse("${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}")

        if (currentTime!!.time >= startTime.time && currentTime.time < endTime.time) {
            return 1
        } else if (currentTime.time - endTime.time >= 0) {
            return -1
        }

        return 0
    }

    fun getTimeDifference(time: String): String {
        val startTime = simpleTimeFormat.parse(time)
        val currentTime = simpleTimeFormat.parse("${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}")

        return when {
            currentTime!!.time - startTime!!.time >= 0 -> simpleTimeFormat.format(Date(currentTime.time - startTime.time))
            else                                       -> simpleTimeFormat.format(Date(startTime.time - currentTime.time))
        }
    }

    fun calculateTimeLeft(deadline: String): Long {
        val today = simpleDateFormat.parse(todayDate)
        val lastDay = simpleDateFormat.parse(deadline)

        val diffInMillis =  lastDay!!.time - today!!.time

        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
    }

    fun fromLetterToHMFormat(context: Context, durationAsLetters: String): String {
        val durations: Array<String> = context.resources.getStringArray(R.array.durations)

        return when (durationAsLetters) {
            durations[0] -> "00:30"
            durations[1] -> "01:00"
            durations[2] -> "01:30"
            durations[3] -> "02:00"
            durations[4] -> "02:30"
            durations[5] -> "03:00"
            durations[6] -> "03:30"
            durations[7] -> "04:00"
            durations[8] -> "05:00"
            durations[9] -> "06:00"
            durations[8] -> "07:00"
            durations[9] -> "08:00"
            else -> throw Exception("Invalid duration!")
        }
    }

}