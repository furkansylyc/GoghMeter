package com.furkometer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MidnightReceiver : BroadcastReceiver() {
    
    private val receiverScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.furkometer.MIDNIGHT_RESET" ||
            intent?.action == Intent.ACTION_BOOT_COMPLETED ||
            intent?.action == "android.intent.action.MY_PACKAGE_REPLACED") {
            
            context?.let { ctx ->
                receiverScope.launch {
                    performDailyReset(ctx)
                }
                scheduleMidnightAlarm(ctx)
            }
        }
    }
    
    companion object {
        fun scheduleMidnightAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, MidnightReceiver::class.java).apply {
                action = "com.furkometer.MIDNIGHT_RESET"
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            val calendar = java.util.Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                add(java.util.Calendar.DAY_OF_YEAR, 1)
                set(java.util.Calendar.HOUR_OF_DAY, 0)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                @Suppress("DEPRECATION")
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }
    }
    
    private suspend fun performDailyReset(context: Context) {
        val repository = PedometerRepository(context.applicationContext as FurkoMeterApplication)
        val state = repository.getCurrentStateSync()
        
        if (state != null) {
            val yesterday = getYesterdayStartTimestamp()
            val yesterdayRecord = repository.getRecordByDate(yesterday)
            
            val finalSteps = state.currentSteps
            val finalDistance = state.distance
            val finalCalories = state.calories
            
            if (yesterdayRecord != null) {
                repository.updateRecord(
                    yesterdayRecord.copy(
                        steps = finalSteps,
                        distance = finalDistance,
                        calories = finalCalories
                    )
                )
            } else {
                repository.insertRecord(
                    com.furkometer.data.StepRecord(
                        date = yesterday,
                        steps = finalSteps,
                        distance = finalDistance,
                        calories = finalCalories
                    )
                )
            }
            
            repository.reset()
            context.sendBroadcast(Intent("com.furkometer.RESET"))
        } else {
            repository.reset()
            context.sendBroadcast(Intent("com.furkometer.RESET"))
        }
    }
    
    private suspend fun getYesterdayStartTimestamp(): Long {
        val calendar = java.util.Calendar.getInstance().apply {
            add(java.util.Calendar.DAY_OF_YEAR, -1)
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }
}

