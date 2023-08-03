package ru.akimslava.bookshelf.models

import kotlinx.serialization.Serializable

@Serializable
data class VolumeInfoData(
    val title: String,
    val subtitle: String? = null,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val pageCount: Int? = null,
    val imageLinks: ImageLinksData? = null,
    // TODO() more info
)