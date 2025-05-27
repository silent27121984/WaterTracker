package com.example.watertracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Модель данных для записи потребления воды
 * @property id уникальный идентификатор записи
 * @property amount количество воды в миллилитрах
 * @property timestamp время потребления воды
 * @property type тип напитка (вода, чай, кофе и т.д.)
 */
@Entity(tableName = "water_entries")
data class WaterEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Int, // в миллилитрах
    val timestamp: Date,
    val type: DrinkType = DrinkType.WATER
)

/**
 * Типы напитков
 */
enum class DrinkType(val displayName: String) {
    WATER("Вода"),
    TEA("Чай"),
    COFFEE("Кофе"),
    JUICE("Сок"),
    OTHER("Другое")
} 