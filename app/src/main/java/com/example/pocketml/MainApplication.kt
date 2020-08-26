package com.example.pocketml

import android.app.Application
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(
                listOf(
                    diModule
                )
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_item_dataset -> {
                    Toast.makeText(this, "Dataset", Toast.LENGTH_LONG)
                    
                    true
                }
                R.id.navigation_item_run -> {
                    Toast.makeText(this, "Run", Toast.LENGTH_LONG)
                    true
                }
                else -> false
            }
        }
    }
}
