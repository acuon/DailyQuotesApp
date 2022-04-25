package dev.acuon.dailyquotesapp.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

// Quotes table in the database
@Entity(tableName = "quotes", primaryKeys = ["id", "quote"])
data class Quote(
    // Primary key for differentiating quotes is an Integer value
    // string is the other primary key to distinguish between quotes
    // while saving them to database.
    var id: Int,

    // other fields
    @SerializedName("q") val quote: String = "",
    @SerializedName("a") val author: String = "",
    @SerializedName("h") val formatted: String = ""
)