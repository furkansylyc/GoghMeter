package com.furkometer

import android.app.Application
import com.furkometer.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar

class PedometerRepository(application: Application) {
    private val database = (application as FurkoMeterApplication).database
    private val stateDao = database.stepCounterStateDao()
    private val recordDao = database.stepRecordDao()

    suspend fun saveInitialStepCount(count: Int) {
        stateDao.updateInitialStepCount(count, hasValue = true)
    }

    suspend fun getInitialStepCount(): Int {
        return stateDao.getStateSync()?.initialStepCount ?: 0
    }

    suspend fun hasInitialValue(): Boolean {
        return stateDao.getStateSync()?.hasInitialValue ?: false
    }

    suspend fun saveCurrentSteps(steps: Int, distance: Double, calories: Int) {
        stateDao.updateStats(steps, distance, calories)

        val today = getTodayStartTimestamp()
        val existingRecord = recordDao.getRecordByDate(today)
        
        if (existingRecord != null) {
            recordDao.updateRecord(
                existingRecord.copy(
                    steps = steps,
                    distance = distance,
                    calories = calories
                )
            )
        } else {
            recordDao.insertRecord(
                StepRecord(
                    date = today,
                    steps = steps,
                    distance = distance,
                    calories = calories
                )
            )
        }
    }

    fun getCurrentSteps(): Flow<Int> {
        return stateDao.getState().map { it?.currentSteps ?: 0 }
    }

    suspend fun getCurrentStepsSync(): Int {
        return stateDao.getStateSync()?.currentSteps ?: 0
    }

    fun getDistance(): Flow<Double> {
        return stateDao.getState().map { it?.distance ?: 0.0 }
    }

    suspend fun getDistanceSync(): Double {
        return stateDao.getStateSync()?.distance ?: 0.0
    }

    fun getCalories(): Flow<Int> {
        return stateDao.getState().map { it?.calories ?: 0 }
    }

    suspend fun getCaloriesSync(): Int {
        return stateDao.getStateSync()?.calories ?: 0
    }

    suspend fun reset() {
        val resetDate = System.currentTimeMillis()
        stateDao.reset(resetDate)
    }

    suspend fun getResetDate(): Long {
        return stateDao.getStateSync()?.lastResetDate ?: 0
    }


    fun getAllRecords(): Flow<List<StepRecord>> {
        return recordDao.getAllRecords()
    }

    suspend fun getRecordsBetween(startDate: Long, endDate: Long): List<StepRecord> {
        return recordDao.getRecordsBetween(startDate, endDate)
    }

    fun getRecentRecords(limit: Int = 7): Flow<List<StepRecord>> {
        val weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        return recordDao.getRecentRecords(weekAgo, limit)
    }

    suspend fun getTotalStepsBetween(startDate: Long, endDate: Long): Int {
        return recordDao.getTotalStepsBetween(startDate, endDate) ?: 0
    }

    suspend fun getCurrentStateSync(): com.furkometer.data.StepCounterState? {
        return stateDao.getStateSync()
    }
    
    suspend fun getRecordByDate(date: Long): com.furkometer.data.StepRecord? {
        return recordDao.getRecordByDate(date)
    }
    
    suspend fun insertRecord(record: com.furkometer.data.StepRecord) {
        recordDao.insertRecord(record)
    }
    
    suspend fun updateRecord(record: com.furkometer.data.StepRecord) {
        recordDao.updateRecord(record)
    }

    private fun getTodayStartTimestamp(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }
}
