package dev.acuon.dailyquotesapp

import android.app.Application
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import dev.acuon.dailyquotesapp.utils.DailyQuoteWorker
import java.util.concurrent.TimeUnit

// Base application class
@HiltAndroidApp
class DailyQuotesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // setup work request for daily motivational quote
        val workRequest = PeriodicWorkRequest
            .Builder(DailyQuoteWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        // enqueue unique periodic work so it doesn't get repeated
        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                getString(R.string.daily_notif_tag),
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

}
