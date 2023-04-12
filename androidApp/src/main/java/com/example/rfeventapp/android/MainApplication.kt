package com.example.rfeventapp.android

import android.app.Application
import com.example.rfeventapp.android.inject.viewModelModule
import com.example.rfeventapp.injection.sharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(sharedModules() + viewModelModule)
        }

    }
}