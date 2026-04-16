package com.kazeyomi.domain.model

data class History(
    val id: Int,
    val mangaId: Int,
    val mangaTitle: String,
    val chapterId: Int,
    val chapterTitle: String,
    val thumbnailUrl: String? = null,
    val lastReadAt: Long,
    val duration: Long = 0
)
