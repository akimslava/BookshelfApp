package ru.akimslava.bookshelf.data

import ru.akimslava.bookshelf.models.SearchRequestData
import ru.akimslava.bookshelf.network.BookshelfApiService

interface BooksRepository {
    suspend fun searchBooks(searchQuery: String): SearchRequestData
}

class NetworkBooksRepository(
    private val bookshelfApiService: BookshelfApiService,
): BooksRepository {
    override suspend fun searchBooks(searchQuery: String): SearchRequestData =
        bookshelfApiService.searchBooks(searchQuery)
}