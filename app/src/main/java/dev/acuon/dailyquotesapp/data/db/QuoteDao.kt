package dev.acuon.dailyquotesapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.acuon.dailyquotesapp.data.model.Quote

// Quote Data Access Object
@Dao
interface QuoteDao {

    // upsert-> insert or update
    // insert the value to database if not present
    // update if already present
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(quote: Quote): Long

    // get all the saved quotes from the local database
    @Query("SELECT * FROM quotes")
    fun getSavedQuotes(): LiveData<List<Quote>>

    // delete the specified quote
    @Delete
    suspend fun deleteSavedQuote(quote: Quote)
}