package com.kazeyomi.domain.model

import com.google.gson.annotations.SerializedName

data class Manga(
    val id: Int,
    val title: String,
    @SerializedName("title_aka")
    val titleAka: List<String>? = null,
    val author: String? = null,
    val artist: String? = null,
    val description: String? = null,
    val genre: List<String>? = null,
    val status: MangaStatus = MangaStatus.UNKNOWN,
    val thumbnailUrl: String? = null,
    @SerializedName("inLibrary")
    val inLibrary: Boolean = false,
    @SerializedName("unreadCount")
    val unreadCount: Int = 0,
    @SerializedName("downloadCount")
    val downloadCount: Int = 0,
    @SerializedName("chapterCount")
    val chapterCount: Int = 0,
    @SerializedName("lastChapterRead")
    val lastChapterRead: Int = 0,
    @SerializedName("sourceId")
    val sourceId: String = "",
    @SerializedName("dateAdded")
    val dateAdded: Long? = null,
    @SerializedName("lastReadAt")
    val lastReadAt: Long? = null,
    @SerializedName("sortDescending")
    val sortDescending: Boolean = true,
    val categories: List<Category> = emptyList()
)

enum class MangaStatus {
    @SerializedName("UNKNOWN")
    UNKNOWN,
    @SerializedName("ONGOING")
    ONGOING,
    @SerializedName("COMPLETED")
    COMPLETED,
    @SerializedName("LICENSED")
    LICENSED,
    @SerializedName("PUBLISHING_FINISHED")
    PUBLISHING_FINISHED,
    @SerializedName("CANCELLED")
    CANCELLED,
    @SerializedName("ON_HIATUS")
    ON_HIATUS
}
