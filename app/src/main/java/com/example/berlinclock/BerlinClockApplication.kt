package com.example.berlinclock

import android.app.Application
import com.example.berlinclock.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BerlinClockApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@BerlinClockApplication)
            modules(appModule)
        }
    }
}
