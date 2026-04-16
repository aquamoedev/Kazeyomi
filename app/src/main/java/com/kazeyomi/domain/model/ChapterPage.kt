package com.kazeyomi.domain.model

import com.google.gson.annotations.SerializedName

data class ChapterPage(
    val index: Int,
    val url: String,
    @SerializedName("pageNumber")
    val pageNumber: Int = 0,
    @SerializedName("imageUrl")
    val imageUrl: String = url,
    val width: Int? = null,
    val height: Int? = null
)
