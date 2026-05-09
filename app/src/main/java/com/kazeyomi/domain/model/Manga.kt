package com.kazeyomi.domain.model

data class Manga(
    val id: Int,
    val title: String,
    val author: String = "",
    val artist: String = "",
    val description: String = "",
    val genre: List<String> = emptyList(),
    val status: MangaStatus = MangaStatus.UNKNOWN,
    val thumbnailUrl: String = "",
    val inLibrary: Boolean = false,
    val unreadCount: Int = 0,
    val downloadCount: Int = 0,
    val chapterCount: Int = 0,
    val sourceId: String = "",
    val dateAdded: Long = 0L,
    val lastReadAt: Long = 0L,
    val updateCount: Int = 0
)

enum class MangaStatus {
    UNKNOWN,
    ONGOING,
    COMPLETED,
    LICENSED,
    CANCELLED,
    ON_HIATUS;

    companion object {
        fun fromString(value: String): MangaStatus = when (value.uppercase()) {
            "ONGOING" -> ONGOING
            "COMPLETED" -> COMPLETED
            "LICENSED" -> LICENSED
            "CANCELLED" -> CANCELLED
            "ON_HIATUS" -> ON_HIATUS
            else -> UNKNOWN
        }
    }
}
