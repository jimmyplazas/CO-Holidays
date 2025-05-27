package dev.alejo.colombian_holidays

import android.app.Application
import dev.alejo.colombian_holidays.di.dataModule
import dev.alejo.colombian_holidays.di.domainModule
import dev.alejo.colombian_holidays.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                uiModule,
                domainModule,
                dataModule
            )
        }
    }
}