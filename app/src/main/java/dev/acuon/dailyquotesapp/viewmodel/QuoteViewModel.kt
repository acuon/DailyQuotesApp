package dev.acuon.dailyquotesapp.viewmodel


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.acuon.dailyquotesapp.data.model.Quote
import dev.acuon.dailyquotesapp.data.repository.QuoteRepository
import dev.acuon.dailyquotesapp.utils.CheckInternet
import dev.acuon.dailyquotesapp.utils.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
@RequiresApi(Build.VERSION_CODES.M)
class QuoteViewModel @Inject constructor(
    private val internet: CheckInternet,
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    // observable variables
    val quote: MutableLiveData<Resource<Quote>> = MutableLiveData()
    val bookmarked: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        // get quote when the view model is called for the first time
        getRandomQuote()
    }

    // function that gets quote from a background thread
    fun getRandomQuote() = viewModelScope.launch {
        bookmarked.postValue(false)
        safeQuotesCall()
    }

    // function that checks for possible phone states before making API requests
    // for example: internet connectivity issues
    private suspend fun safeQuotesCall() {
        try {
            if (internet.hasInternetConnection()) {
                quote.postValue(Resource.Loading())
                val response = quoteRepository.getRandomQuote()
                quote.postValue(handleQuoteResponse(response))
            } else {
                quote.postValue(Resource.Error("No internet connection."))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> quote.postValue(Resource.Error("Network Failure"))
                else -> quote.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    // convert API response to resource type that can be used to fetch status of response
    private fun handleQuoteResponse(response: Response<List<Quote>>): Resource<Quote> {
        if (response.isSuccessful) {
            return Resource.Success(response.body()!![0])
        }
        return Resource.Error(response.message())
    }

    // save a particular quote to the database
    fun saveQuote(quote: Quote) = viewModelScope.launch {
        quoteRepository.upsert(quote)
        bookmarked.postValue(true)
    }

    // get all the saved quotes from the database
    fun getSavedQuotes() = quoteRepository.getSavedQuotes()

    // delete a particular quote
    fun deleteQuote(quote: Quote) = viewModelScope.launch {
        quoteRepository.deleteQuote(quote)
        bookmarked.postValue(false)
    }
}