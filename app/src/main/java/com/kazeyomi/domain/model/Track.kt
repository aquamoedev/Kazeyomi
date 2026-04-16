package com.kazeyomi.domain.model

import com.google.gson.annotations.SerializedName

data class Track(
    val id: Long,
    @SerializedName("mangaId")
    val mangaId: Int,
    @SerializedName("syncId")
    val syncId: Int,
    @SerializedName("mediaId")
    val mediaId: Long,
    @SerializedName("trackingUrl")
    val trackingUrl: String = "",
    @SerializedName("title")
    val title: String = "",
    val lastChapterRead: Int = 0,
    val totalChapters: Int = 0,
    val score: Float = 0f,
    val status: Int = 0,
    @SerializedName("displayScore")
    val displayScore: String = "",
    val startedReading: String? = null,
    val finishedReading: String? = null,
    @SerializedName("lastUpdated")
    val lastUpdated: Long = 0
)
