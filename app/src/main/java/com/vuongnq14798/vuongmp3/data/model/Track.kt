package com.vuongnq14798.vuongmp3.data.model

data class Track(
    val id: Int,
    val title: String,
    val duration: Int,
    val likesCount: Int = 0,
    val isDownload: Boolean,
    val streamUrl: String,
    val username: String,
    val artworkUrl: String?,
    val downloadUrl: String?,
    val artist: String?,
    val description: String?
)
