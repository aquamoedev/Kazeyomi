package com.kazeyomi.domain.model

import com.google.gson.annotations.SerializedName

data class Source(
    val id: String,
    val name: String,
    val iconUrl: String? = null,
    val websiteBaseUrl: String = "",
    val displayName: String = name,
    val language: String = "",
    @SerializedName("isConfigurable")
    val isConfigurable: Boolean = false,
    @SerializedName("isInstalled")
    val isInstalled: Boolean = false,
    @SerializedName("isNsfw")
    val isNsfw: Boolean = false,
    @SerializedName("hasCloudFlare")
    val hasCloudFlare: Boolean = false,
    val version: String = "",
    @SerializedName("requiresStudio")
    val requiresStudio: Boolean = false,
    val categories: List<String> = emptyList()
)
