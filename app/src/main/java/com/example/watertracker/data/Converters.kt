package com.example.watertracker.data

import androidx.room.TypeConverter
import java.util.Date

/**
 * Конвертеры для работы с датами в Room
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
} 