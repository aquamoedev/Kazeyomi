package com.kazeyomi.domain.model

data class Download(
    val chapterId: Int,
    val mangaId: Int,
    val mangaTitle: String = "",
    val chapterName: String = "",
    val state: DownloadState = DownloadState.NOT_DOWNLOADED,
    val progress: Float = 0f,
    val downloadedPages: Int = 0,
    val totalPages: Int = 0
)
