package dev.acuon.dailyquotesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.acuon.dailyquotesapp.data.model.Quote

// Database configurations
@Database(entities = [Quote::class],version = 1)
abstract class QuoteRoomDatabase : RoomDatabase() {
    // DAO object
    abstract fun getQuoteDao(): QuoteDao
}
