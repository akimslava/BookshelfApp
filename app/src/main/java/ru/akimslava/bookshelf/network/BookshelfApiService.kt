package ru.akimslava.bookshelf.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.akimslava.bookshelf.models.SearchRequestData

interface BookshelfApiService {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") searchQuery: String): SearchRequestData
}