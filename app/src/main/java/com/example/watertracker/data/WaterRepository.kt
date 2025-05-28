package com.example.watertracker.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import java.util.Calendar

/**
 * Репозиторий для работы с данными о потреблении воды
 */
class WaterRepository(private val waterEntryDao: WaterEntryDao) {
    
    // Получить все записи
    fun getAllEntries(): Flow<List<WaterEntry>> = waterEntryDao.getAllEntries()

    // Получить записи за определенный период
    fun getEntriesForDateRange(startDate: Date, endDate: Date): Flow<List<WaterEntry>> =
        waterEntryDao.getEntriesForDateRange(startDate, endDate)

    // Получить общее количество воды за период
    fun getTotalAmountForDateRange(startDate: Date, endDate: Date): Flow<Int?> =
        waterEntryDao.getTotalAmountForDateRange(startDate, endDate)

    // Получить статистику за период
    fun getStatisticsForDateRange(startDate: Date, endDate: Date, goal: Int): Flow<WaterStatistics> {
        return waterEntryDao.getEntriesForDateRange(startDate, endDate).map { entries ->
            if (entries.isEmpty()) {
                WaterStatistics(
                    totalAmount = 0,
                    averageAmount = 0,
                    maxAmount = 0,
                    minAmount = 0,
                    daysWithData = 0,
                    goalAchievedDays = 0
                )
            } else {
                // Группируем записи по дням
                val dailyTotals = entries.groupBy { entry -> 
                    val calendar = Calendar.getInstance()
                    calendar.time = entry.timestamp
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    calendar.time
                }.mapValues { (_, dayEntries) -> dayEntries.sumOf { it.amount } }

                val totalAmount = dailyTotals.values.sum()
                val daysWithData = dailyTotals.size
                val goalAchievedDays = dailyTotals.count { (_, amount) -> amount >= goal }

                WaterStatistics(
                    totalAmount = totalAmount,
                    averageAmount = if (daysWithData > 0) totalAmount / daysWithData else 0,
                    maxAmount = dailyTotals.values.maxOrNull() ?: 0,
                    minAmount = dailyTotals.values.minOrNull() ?: 0,
                    daysWithData = daysWithData,
                    goalAchievedDays = goalAchievedDays
                )
            }
        }
    }

    // Получить статистику за день
    fun getStatisticsForDate(date: Date, goal: Int): Flow<WaterStatistics> {
        val startOfDay = date.getStartOfDay()
        val endOfDay = date.getEndOfDay()
        return waterEntryDao.getEntriesForDateRange(startOfDay, endOfDay).map { entries ->
            if (entries.isEmpty()) {
                WaterStatistics(
                    totalAmount = 0,
                    averageAmount = 0,
                    maxAmount = 0,
                    minAmount = 0,
                    daysWithData = 0,
                    goalAchievedDays = 0
                )
            } else {
                val totalAmount = entries.sumOf { it.amount }
                WaterStatistics(
                    totalAmount = totalAmount,
                    averageAmount = totalAmount,
                    maxAmount = entries.maxOf { it.amount },
                    minAmount = entries.minOf { it.amount },
                    daysWithData = 1,
                    goalAchievedDays = if (totalAmount >= goal) 1 else 0
                )
            }
        }
    }

    // Получить статистику за неделю
    fun getStatisticsForWeek(date: Date, goal: Int): Flow<WaterStatistics> {
        val startOfWeek = date.getStartOfWeek()
        val endOfWeek = date.getEndOfWeek()
        return getStatisticsForDateRange(startOfWeek, endOfWeek, goal)
    }

    // Получить статистику за месяц
    fun getStatisticsForMonth(date: Date, goal: Int): Flow<WaterStatistics> {
        val startOfMonth = date.getStartOfMonth()
        val endOfMonth = date.getEndOfMonth()
        return getStatisticsForDateRange(startOfMonth, endOfMonth, goal)
    }

    // Добавить новую запись
    suspend fun insertEntry(entry: WaterEntry) = waterEntryDao.insertEntry(entry)

    // Обновить запись
    suspend fun updateEntry(entry: WaterEntry) = waterEntryDao.updateEntry(entry)

    // Удалить запись
    suspend fun deleteEntry(entry: WaterEntry) = waterEntryDao.deleteEntry(entry)

    // Удалить все записи за период
    suspend fun deleteEntriesForDateRange(startDate: Date, endDate: Date) =
        waterEntryDao.deleteEntriesForDateRange(startDate, endDate)
} 