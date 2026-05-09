package com.kazeyomi.domain.model

data class Source(
    val id: String,
    val name: String,
    val iconUrl: String = "",
    val displayName: String = "",
    val language: String = "",
    val isConfigurable: Boolean = false,
    val isInstalled: Boolean = true,
    val isNsfw: Boolean = false,
    val version: String = "",
    val categories: List<String> = emptyList()
)
