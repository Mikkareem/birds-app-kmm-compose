package com.techullurgy.composekmmtest

import android.app.Application
import di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BirdsInfoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BirdsInfoApplication)
            modules(appModule)
        }
    }
}