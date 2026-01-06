package io.github.alest.photomapper.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver


object DatabaseProvider {
    lateinit var app: Application

    val database: Database by lazy {
        val driver = AndroidSqliteDriver(Database.Schema, app, "photomapper.db")
        Database(driver)
    }
}