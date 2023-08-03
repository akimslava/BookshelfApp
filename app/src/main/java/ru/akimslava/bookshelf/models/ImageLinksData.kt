package ru.akimslava.bookshelf.models

import kotlinx.serialization.Serializable

@Serializable
data class ImageLinksData(
    val thumbnail: String,
)