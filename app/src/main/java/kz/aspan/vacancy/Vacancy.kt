package kz.aspan.vacancy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Vacancy : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}