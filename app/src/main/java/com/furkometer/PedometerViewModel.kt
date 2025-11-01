package com.furkometer

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PedometerViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: PedometerRepository = PedometerRepository(application)
    
    val steps: StateFlow<Int> = repository.getCurrentSteps()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val distance: StateFlow<Double> = repository.getDistance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    val calories: StateFlow<Int> = repository.getCalories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val stepHistory: StateFlow<List<com.furkometer.data.StepRecord>> = repository.getAllRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val stepsUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.furkometer.STEPS_UPDATED") {
            }
        }
    }

    init {

        val filter = IntentFilter("com.furkometer.STEPS_UPDATED")
        ContextCompat.registerReceiver(
            application,
            stepsUpdateReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    fun startStepCounter() {
        val intent = Intent(getApplication(), StepCounterService::class.java).apply {
            action = StepCounterService.ACTION_START
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            getApplication<Application>().startForegroundService(intent)
        } else {
            getApplication<Application>().startService(intent)
        }
    }

    fun stopStepCounter() {
        val intent = Intent(getApplication(), StepCounterService::class.java).apply {
            action = StepCounterService.ACTION_STOP
        }
        getApplication<Application>().stopService(intent)
    }

    fun loadData() {

    }

    fun resetSteps() {
        viewModelScope.launch {
            val intent = Intent(getApplication(), StepCounterService::class.java).apply {
                action = "com.furkometer.RESET"
            }
            getApplication<Application>().sendBroadcast(intent)
            

            repository.reset()
        }
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unregisterReceiver(stepsUpdateReceiver)
    }
}
