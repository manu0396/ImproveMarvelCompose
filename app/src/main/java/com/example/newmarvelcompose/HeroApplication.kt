package com.example.newmarvelcompose

import android.app.Application
import com.example.newmarvelcompose.di.AppComponent
import com.example.newmarvelcompose.di.DaggerAppComponent
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class HeroApplication : Application () {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()

        appComponent = initDagger()
        Timber.plant(Timber.DebugTree())
    }

    private fun initDagger() = DaggerAppComponent.builder().application(this).build()
}