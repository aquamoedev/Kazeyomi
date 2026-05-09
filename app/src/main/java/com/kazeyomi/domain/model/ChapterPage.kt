package com.kazeyomi.domain.model

data class ChapterPage(
    val index: Int,
    val url: String,
    val pageNumber: Int = 0,
    val imageUrl: String = ""
)
