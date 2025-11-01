package com.furkometer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_counter_state")
data class StepCounterState(
    @PrimaryKey val id: Int = 1,
    val initialStepCount: Int = 0,
    val hasInitialValue: Boolean = false,
    val currentSteps: Int = 0,
    val distance: Double = 0.0,
    val calories: Int = 0,
    val lastResetDate: Long = 0
)

