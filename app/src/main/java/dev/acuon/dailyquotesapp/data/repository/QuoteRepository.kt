package dev.acuon.dailyquotesapp.data.repository

import dev.acuon.dailyquotesapp.data.db.QuoteDao
import dev.acuon.dailyquotesapp.data.api.ApiService
import dev.acuon.dailyquotesapp.data.model.Quote
import javax.inject.Inject

// bridge between View Model, API and Database
class QuoteRepository @Inject constructor(
    private val dao: QuoteDao,
    private val api: ApiService
) {

    suspend fun getRandomQuote() = api.getRandomQuote()

    suspend fun getQuoteOfTheDay() = api.getQuoteOfTheDay()

    suspend fun upsert(quote: Quote) = dao.upsert(quote)

    suspend fun deleteQuote(quote: Quote) =
        dao.deleteSavedQuote(quote)

    fun getSavedQuotes() = dao.getSavedQuotes()
}