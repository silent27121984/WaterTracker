package com.example.watertracker.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.watertracker.data.DrinkType
import com.example.watertracker.data.WaterDatabase
import com.example.watertracker.data.WaterEntry
import com.example.watertracker.data.WaterRepository
import com.example.watertracker.data.WaterStatistics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

/**
 * ViewModel для управления данными о потреблении воды
 */
class WaterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WaterRepository
    private val _dailyGoal = MutableStateFlow(2000) // Цель по умолчанию - 2 литра
    val dailyGoal: StateFlow<Int> = _dailyGoal.asStateFlow()

    // Статистика
    private val _selectedDate = MutableStateFlow(Date())
    val selectedDate: StateFlow<Date> = _selectedDate.asStateFlow()

    private val _statistics = MutableStateFlow(WaterStatistics())
    val statistics: StateFlow<WaterStatistics> = _statistics.asStateFlow()

    init {
        val waterDao = WaterDatabase.getDatabase(application).waterEntryDao()
        repository = WaterRepository(waterDao)
        updateStatistics()
    }

    // Получить записи за сегодня
    fun getTodayEntries() = repository.getEntriesForDateRange(
        getStartOfDay(),
        getEndOfDay()
    )

    // Получить общее количество воды за сегодня
    fun getTodayTotal() = repository.getTotalAmountForDateRange(
        getStartOfDay(),
        getEndOfDay()
    ).map { it ?: 0 }

    // Добавить новую запись
    fun addWaterEntry(amount: Int, type: DrinkType = DrinkType.WATER) {
        viewModelScope.launch {
            val entry = WaterEntry(
                amount = amount,
                timestamp = Date(),
                type = type
            )
            repository.insertEntry(entry)
        }
    }

    // Обновить цель
    fun updateDailyGoal(goal: Int) {
        _dailyGoal.value = goal
        updateStatistics()
    }

    // Обновить выбранную дату
    fun updateSelectedDate(date: Date) {
        _selectedDate.value = date
        updateStatistics()
    }

    // Обновить статистику
    private fun updateStatistics() {
        viewModelScope.launch {
            repository.getStatisticsForDate(selectedDate.value, dailyGoal.value)
                .collect { stats ->
                    _statistics.value = stats
                }
        }
    }

    // Получить статистику за неделю
    fun getWeeklyStatistics() = repository.getStatisticsForWeek(selectedDate.value, dailyGoal.value)

    // Получить статистику за месяц
    fun getMonthlyStatistics() = repository.getStatisticsForMonth(selectedDate.value, dailyGoal.value)

    // Вспомогательные функции для работы с датами
    private fun getStartOfDay(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    private fun getEndOfDay(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
} 