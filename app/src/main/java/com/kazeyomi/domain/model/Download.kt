package com.kazeyomi.domain.model

import com.google.gson.annotations.SerializedName

data class Download(
    val chapterId: Int,
    val mangaId: Int,
    val mangaTitle: String,
    val chapterName: String,
    val state: DownloadState = DownloadState.NOT_DOWNLOADED,
    val progress: Int = 0,
    @SerializedName("downloadedPages")
    val downloadedPages: Int = 0,
    @SerializedName("totalPages")
    val totalPages: Int = 0,
    val retryCount: Int = 0
)
