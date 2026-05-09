package com.kazeyomi.domain.model

data class Chapter(
    val id: Int,
    val mangaId: Int,
    val title: String = "",
    val name: String = "",
    val chapter: Float = 0f,
    val read: Boolean = false,
    val bookmarked: Boolean = false,
    val lastPageRead: Int = 0,
    val pageCount: Int = 0,
    val uploadDate: Long = 0L,
    val downloadState: DownloadState = DownloadState.NOT_DOWNLOADED,
    val scanlator: String = ""
)

enum class ChapterStatus {
    LOADING,
    ERROR,
    READY
}

enum class DownloadState {
    NOT_DOWNLOADED,
    QUEUED,
    DOWNLOADING,
    DOWNLOADED,
    ERROR;

    companion object {
        fun fromString(value: String): DownloadState = when (value.uppercase()) {
            "QUEUED" -> QUEUED
            "DOWNLOADING" -> DOWNLOADING
            "DOWNLOADED" -> DOWNLOADED
            "ERROR" -> ERROR
            else -> NOT_DOWNLOADED
        }
    }
}
