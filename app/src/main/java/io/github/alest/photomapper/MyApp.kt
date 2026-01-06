package io.github.alest.photomapper

import android.app.Application
import io.github.alest.photomapper.db.DatabaseProvider

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.app = this
    }
}