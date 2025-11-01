package com.furkometer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [StepRecord::class, StepCounterState::class],
    version = 1,
    exportSchema = false
)
abstract class PedometerDatabase : RoomDatabase() {
    
    abstract fun stepRecordDao(): StepRecordDao
    abstract fun stepCounterStateDao(): StepCounterStateDao
    
    companion object {
        @Volatile
        private var INSTANCE: PedometerDatabase? = null
        
        fun getDatabase(context: Context): PedometerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PedometerDatabase::class.java,
                    "pedometer_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
                scope.launch {
                    if (instance.stepCounterStateDao().getStateSync() == null) {
                        instance.stepCounterStateDao().insertState(StepCounterState())
                    }
                }
                
                instance
            }
        }
    }
}

