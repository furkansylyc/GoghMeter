package com.furkometer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "step_records")
data class StepRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long,
    val steps: Int,
    val distance: Double,
    val calories: Int
) {
    fun getDateString(): String {
        val dateObj = Date(date)
        val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return format.format(dateObj)
    }
}

