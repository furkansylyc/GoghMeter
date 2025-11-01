package com.furkometer

import android.app.Application
import com.furkometer.data.PedometerDatabase

class FurkoMeterApplication : Application() {
    
    val database by lazy { PedometerDatabase.getDatabase(this) }
}

