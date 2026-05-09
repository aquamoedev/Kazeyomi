package com.kazeyomi.domain.model

data class Category(
    val id: Int = 0,
    val name: String,
    val order: Int = 0,
    val size: Int = 0,
    val default: Boolean = false,
    val isHidden: Boolean = false
)
