package ru.akimslava.bookshelf.models

import kotlinx.serialization.Serializable

@Serializable
data class BookData(
    val selfLink: String,
    val volumeInfo: VolumeInfoData,
)