package manu.lucas.dev

import android.app.Application
import manu.lucas.dev.di.AppComponent
import manu.lucas.dev.di.DaggerAppComponent
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