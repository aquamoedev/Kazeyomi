package com.kazeyomi.domain.model

data class Extension(
    val name: String,
    val version: String = "",
    val pkgName: String,
    val iconUrl: String = "",
    val lang: String = "",
    val type: ExtensionType = ExtensionType.UNKNOWN,
    val isInstalled: Boolean = false,
    val isObsolete: Boolean = false,
    val hasUpdate: Boolean = false,
    val sources: List<ExtensionSource> = emptyList()
)

data class ExtensionSource(
    val id: String,
    val name: String,
    val lang: String = "",
    val isNsfw: Boolean = false
)

enum class ExtensionType {
    UNKNOWN,
    SOURCE,
    TRACKER;

    companion object {
        fun fromString(value: String): ExtensionType = when (value.uppercase()) {
            "SOURCE" -> SOURCE
            "TRACKER" -> TRACKER
            else -> UNKNOWN
        }
    }
}
