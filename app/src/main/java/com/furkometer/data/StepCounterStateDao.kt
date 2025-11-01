package com.furkometer.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StepCounterStateDao {
    
    @Query("SELECT * FROM step_counter_state WHERE id = 1 LIMIT 1")
    fun getState(): Flow<StepCounterState?>
    
    @Query("SELECT * FROM step_counter_state WHERE id = 1 LIMIT 1")
    suspend fun getStateSync(): StepCounterState?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertState(state: StepCounterState)
    
    @Update
    suspend fun updateState(state: StepCounterState)
    
    @Query("UPDATE step_counter_state SET currentSteps = :steps, distance = :distance, calories = :calories WHERE id = 1")
    suspend fun updateStats(steps: Int, distance: Double, calories: Int)
    
    @Query("UPDATE step_counter_state SET initialStepCount = :count, hasInitialValue = :hasValue WHERE id = 1")
    suspend fun updateInitialStepCount(count: Int, hasValue: Boolean)
    
    @Query("UPDATE step_counter_state SET initialStepCount = 0, hasInitialValue = 0, currentSteps = 0, distance = 0.0, calories = 0, lastResetDate = :resetDate WHERE id = 1")
    suspend fun reset(resetDate: Long)
}

