package com.furkometer

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Build.VERSION_CODES
import android.os.IBinder
import android.content.Context.RECEIVER_NOT_EXPORTED
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class StepCounterService : Service(), SensorEventListener {
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var sensorManager: SensorManager
    private lateinit var repository: PedometerRepository
    private var stepCounterSensor: Sensor? = null
    private var initialStepCount = 0
    private var hasInitialValue = false

    private val averageStepLengthMeters = 0.75
    private val caloriesPer1000Steps = 45

    companion object {
        private const val CHANNEL_ID = "step_counter_channel"
        private const val NOTIFICATION_ID = 1
        
        const val ACTION_START = "com.furkometer.START"
        const val ACTION_STOP = "com.furkometer.STOP"
    }


    private val resetReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.furkometer.RESET") {
                resetSteps()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                startForegroundService()
                MidnightReceiver.scheduleMidnightAlarm(this)
            }
            ACTION_STOP -> {
                stopSelf()
            }
        }
        return START_STICKY
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        repository = PedometerRepository(applicationContext as FurkoMeterApplication)
        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(resetReceiver, IntentFilter("com.furkometer.RESET"), RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            registerReceiver(resetReceiver, IntentFilter("com.furkometer.RESET"))
        }
        
        serviceScope.launch {
            if (repository.hasInitialValue()) {
                initialStepCount = repository.getInitialStepCount()
                hasInitialValue = true
            }
        }
    }

    private fun startForegroundService() {
        startStepCounter()

        serviceScope.launch {
            val steps = repository.getCurrentStepsSync()
            val distance = repository.getDistanceSync()
            val calories = repository.getCaloriesSync()

            android.os.Handler(android.os.Looper.getMainLooper()).post {
                startForeground(NOTIFICATION_ID, createNotification(steps, distance, calories))
            }
        }
    }

    private fun startStepCounter() {
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepCounterSensor != null) {
            sensorManager.registerListener(
                this,
                stepCounterSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            if (!hasInitialValue) {
                initialStepCount = event.values[0].toInt()
                hasInitialValue = true
                serviceScope.launch {
                    repository.saveInitialStepCount(initialStepCount)
                }
            }
            
            val currentSteps = event.values[0].toInt() - initialStepCount
            val distanceMeters = currentSteps * averageStepLengthMeters
            val distance = distanceMeters / 1000.0
            val calories = (currentSteps / 1000.0 * caloriesPer1000Steps).toInt()
            serviceScope.launch {
                repository.saveCurrentSteps(currentSteps, distance, calories)
            }

            updateNotification(currentSteps, distance, calories)

            sendBroadcast(Intent("com.furkometer.STEPS_UPDATED").apply {
                putExtra("steps", currentSteps)
                putExtra("distance", distance)
                putExtra("calories", calories)
            })
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Adım Sayar",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Adımlarınızı arka planda sayar"
                setShowBadge(false)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(steps: Int = 0, distance: Double = 0.0, calories: Int = 0): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("GoghMeter")
            .setContentText("$steps adım • ${String.format("%.2f", distance)} km • $calories kcal")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setShowWhen(false)
            .build()
    }

    private fun updateNotification(steps: Int, distance: Double, calories: Int) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, createNotification(steps, distance, calories))
    }

    fun resetSteps() {
        hasInitialValue = false
        initialStepCount = 0
        serviceScope.launch {
            repository.reset()
        }
        sensorManager.unregisterListener(this)
        startStepCounter()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(resetReceiver)
        } catch (e: Exception) {
        }
        sensorManager.unregisterListener(this)
    }
}

