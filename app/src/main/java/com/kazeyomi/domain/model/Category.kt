package com.kazeyomi.domain.model

import com.google.gson.annotations.SerializedName

data class Category(
    val id: Int = 0,
    val name: String,
    val order: Int = 0,
    val size: Int = 0,
    val default: Boolean = false,
    @SerializedName("isHidden")
    val isHidden: Boolean = false
)
