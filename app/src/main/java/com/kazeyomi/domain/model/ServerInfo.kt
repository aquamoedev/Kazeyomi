package com.kazeyomi.domain.model

data class ServerInfo(
    val version: String = "",
    val versionName: String = "",
    @Suppress("PropertyName")
    val apiVersion: Int = 0,
    val databaseVersion: Int = 0,
    @Suppress("PropertyName")
    val dataFolder: String = "",
    @Suppress("PropertyName")
    val iconUrl: String? = null
)
