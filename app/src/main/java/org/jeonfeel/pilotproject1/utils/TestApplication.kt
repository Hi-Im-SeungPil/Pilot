package org.jeonfeel.pilotproject1.utils

import android.app.Application
import org.jeonfeel.pilotproject1.data.sharedpreferences.Shared

class TestApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Shared.create(this.applicationContext)
    }
}