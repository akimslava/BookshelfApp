package ru.akimslava.bookshelf.models

import kotlinx.serialization.Serializable

@Serializable
data class SearchRequestData(
    val totalItems: Int,
    val items: List<BookData>? = null,
)