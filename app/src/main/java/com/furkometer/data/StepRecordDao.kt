package com.furkometer.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StepRecordDao {
    
    @Query("SELECT * FROM step_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<StepRecord>>
    
    @Query("SELECT * FROM step_records WHERE date = :date LIMIT 1")
    suspend fun getRecordByDate(date: Long): StepRecord?
    
    @Query("SELECT * FROM step_records WHERE date >= :startDate AND date < :endDate ORDER BY date ASC")
    suspend fun getRecordsBetween(startDate: Long, endDate: Long): List<StepRecord>
    
    @Query("SELECT * FROM step_records WHERE date >= :startDate ORDER BY date DESC LIMIT :limit")
    fun getRecentRecords(startDate: Long, limit: Int): Flow<List<StepRecord>>
    
    @Query("SELECT SUM(steps) FROM step_records WHERE date >= :startDate AND date < :endDate")
    suspend fun getTotalStepsBetween(startDate: Long, endDate: Long): Int?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: StepRecord): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecords(records: List<StepRecord>)
    
    @Update
    suspend fun updateRecord(record: StepRecord)
    
    @Delete
    suspend fun deleteRecord(record: StepRecord)
    
    @Query("DELETE FROM step_records WHERE date < :beforeDate")
    suspend fun deleteOldRecords(beforeDate: Long)
}

