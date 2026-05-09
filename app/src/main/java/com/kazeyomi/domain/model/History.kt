package com.kazeyomi.domain.model

data class History(
    val id: Int = 0,
    val mangaId: Int,
    val mangaTitle: String = "",
    val chapterId: Int,
    val chapterTitle: String = "",
    val thumbnailUrl: String = "",
    val lastReadAt: Long = 0L
)
