package com.example.watertracker.data

import java.util.Calendar
import java.util.Date

/**
 * Класс для работы со статистикой потребления воды
 */
data class WaterStatistics(
    val totalAmount: Int = 0,
    val averageAmount: Int = 0,
    val maxAmount: Int = 0,
    val minAmount: Int = 0,
    val daysWithData: Int = 0,
    val goalAchievedDays: Int = 0
)

/**
 * Расширение для работы с датами
 */
fun Date.getStartOfDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

fun Date.getEndOfDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.time
}

fun Date.getStartOfWeek(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    return calendar.time.getStartOfDay()
}

fun Date.getEndOfWeek(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek + 6)
    return calendar.time.getEndOfDay()
}

fun Date.getStartOfMonth(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.time.getStartOfDay()
}

fun Date.getEndOfMonth(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    return calendar.time.getEndOfDay()
} 