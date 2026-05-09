package com.kazeyomi.domain.model

data class ServerInfo(
    val version: String = "",
    val versionName: String = "",
    val apiVersion: Int = 0,
    val databaseVersion: Int = 0,
    val dataFolder: String = "",
    val iconUrl: String? = null
)
