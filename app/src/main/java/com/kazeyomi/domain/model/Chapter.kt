package com.kazeyomi.domain.model

import com.google.gson.annotations.SerializedName

data class Chapter(
    val id: Int,
    @SerializedName("mangaId")
    val mangaId: Int,
    val sourceId: String = "",
    val url: String = "",
    val title: String = "",
    val name: String = "",
    val volume: String = "",
    val chapter: String = "",
    @SerializedName("scanlator")
    val scanlator: String? = null,
    val read: Boolean = false,
    val bookmarked: Boolean = false,
    @SerializedName("lastPageRead")
    val lastPageRead: Int = 0,
    @SerializedName("pageCount")
    val pageCount: Int = 0,
    val status: ChapterStatus = ChapterStatus.READY,
    @SerializedName("fetchAt")
    val fetchAt: Long = 0,
    @SerializedName("uploadDate")
    val uploadDate: Long = 0,
    @SerializedName("downloadState")
    val downloadState: DownloadState = DownloadState.NOT_DOWNLOADED,
    @SerializedName("downloadProgress")
    val downloadProgress: Int = 0
)

enum class ChapterStatus {
    @SerializedName("LOADING")
    LOADING,
    @SerializedName("ERROR")
    ERROR,
    @SerializedName("READY")
    READY
}

enum class DownloadState {
    @SerializedName("STATE_NON")
    NOT_DOWNLOADED,
    @SerializedName("STATE_QUEUED")
    QUEUED,
    @SerializedName("STATE_DOWNLOADING")
    DOWNLOADING,
    @SerializedName("STATE_DOWNLOADED")
    DOWNLOADED,
    @SerializedName("STATE_ERROR")
    ERROR
}
