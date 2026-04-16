package com.kazeyomi.domain.model

import com.google.gson.annotations.SerializedName

data class Extension(
    val name: String,
    val version: String,
    val pkgName: String,
    val iconUrl: String? = null,
    val lang: String = "",
    val type: ExtensionType = ExtensionType.UNKNOWN,
    @SerializedName("isInstalled")
    val isInstalled: Boolean = false,
    @SerializedName("isObsolete")
    val isObsolete: Boolean = false,
    @SerializedName("hasUpdate")
    val hasUpdate: Boolean = false,
    @SerializedName("installButton")
    val installButton: InstallButton = InstallButton.INSTALLED,
    val sources: List<String> = emptyList()
)

enum class ExtensionType {
    @SerializedName("UNKNOWN")
    UNKNOWN,
    @SerializedName("EXTENSION")
    EXTENSION,
    @SerializedName("NSFW")
    NSFW,
    @SerializedName("SOURCES")
    SOURCES
}

enum class InstallButton {
    @SerializedName("INSTALL")
    INSTALL,
    @SerializedName("UPDATE")
    UPDATE,
    @SerializedName("UNINSTALL")
    UNINSTALL,
    @SerializedName("INSTALLED")
    INSTALLED
}
