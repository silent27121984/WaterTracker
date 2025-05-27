package com.example.watertracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Data Access Object для работы с записями потребления воды
 */
@Dao
interface WaterEntryDao {
    @Query("SELECT * FROM water_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<WaterEntry>>

    @Query("SELECT * FROM water_entries WHERE timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getEntriesForDateRange(startDate: Date, endDate: Date): Flow<List<WaterEntry>>

    @Query("SELECT SUM(amount) FROM water_entries WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getTotalAmountForDateRange(startDate: Date, endDate: Date): Flow<Int?>

    @Query("SELECT AVG(amount) FROM water_entries WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getAverageAmountForDateRange(startDate: Date, endDate: Date): Flow<Double?>

    @Query("SELECT MAX(amount) FROM water_entries WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getMaxAmountForDateRange(startDate: Date, endDate: Date): Flow<Int?>

    @Query("SELECT MIN(amount) FROM water_entries WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getMinAmountForDateRange(startDate: Date, endDate: Date): Flow<Int?>

    @Query("SELECT COUNT(DISTINCT date(timestamp/1000, 'unixepoch')) FROM water_entries WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getDaysWithDataForDateRange(startDate: Date, endDate: Date): Flow<Int?>

    @Query("SELECT COUNT(DISTINCT date(timestamp/1000, 'unixepoch')) FROM water_entries WHERE timestamp BETWEEN :startDate AND :endDate AND amount >= :goal")
    fun getDaysWithGoalAchievedForDateRange(startDate: Date, endDate: Date, goal: Int): Flow<Int?>

    @Query("SELECT SUM(amount) FROM water_entries WHERE date(timestamp/1000, 'unixepoch') = date(:date/1000, 'unixepoch')")
    fun getTotalAmountForDate(date: Date): Flow<Int?>

    @Insert
    suspend fun insertEntry(entry: WaterEntry)

    @Update
    suspend fun updateEntry(entry: WaterEntry)

    @Delete
    suspend fun deleteEntry(entry: WaterEntry)

    @Query("DELETE FROM water_entries WHERE timestamp BETWEEN :startDate AND :endDate")
    suspend fun deleteEntriesForDateRange(startDate: Date, endDate: Date)
} 