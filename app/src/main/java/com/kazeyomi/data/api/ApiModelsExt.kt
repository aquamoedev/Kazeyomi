package com.kazeyomi.data.api

import com.google.gson.annotations.SerializedName
import com.kazeyomi.domain.model.*

// ── Extension DTOs ──

data class ExtensionsResponse(
    @SerializedName("extensions") val extensions: List<ExtensionDto>?
)

data class ExtensionDto(
    @SerializedName("name") val name: String?,
    @SerializedName("version") val version: String?,
    @SerializedName("pkgName") val pkgName: String?,
    @SerializedName("iconUrl") val iconUrl: String?,
    @SerializedName("lang") val lang: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("isInstalled") val isInstalled: Boolean?,
    @SerializedName("isObsolete") val isObsolete: Boolean?,
    @SerializedName("hasUpdate") val hasUpdate: Boolean?,
    @SerializedName("sources") val sources: List<ExtensionSourceDto>?
)

data class ExtensionSourceDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("lang") val lang: String?,
    @SerializedName("isNsfw") val isNsfw: Boolean?
)

fun ExtensionDto.toExtension(): Extension = Extension(
    name = name ?: "",
    version = version ?: "",
    pkgName = pkgName ?: "",
    iconUrl = iconUrl ?: "",
    lang = lang ?: "",
    type = ExtensionType.fromString(type ?: "UNKNOWN"),
    isInstalled = isInstalled ?: false,
    isObsolete = isObsolete ?: false,
    hasUpdate = hasUpdate ?: false,
    sources = sources?.map { it.toExtensionSource() } ?: emptyList()
)

fun ExtensionSourceDto.toExtensionSource(): ExtensionSource = ExtensionSource(
    id = id ?: "",
    name = name ?: "",
    lang = lang ?: "",
    isNsfw = isNsfw ?: false
)

// ── Download DTOs ──

data class DownloadsResponse(
    @SerializedName("downloads") val downloads: List<DownloadDto>?
)

data class DownloadDto(
    @SerializedName("chapterId") val chapterId: Int?,
    @SerializedName("mangaId") val mangaId: Int?,
    @SerializedName("mangaTitle") val mangaTitle: String?,
    @SerializedName("chapterName") val chapterName: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("progress") val progress: Float?,
    @SerializedName("downloadedPages") val downloadedPages: Int?,
    @SerializedName("totalPages") val totalPages: Int?
)

fun DownloadDto.toDownload(): Download = Download(
    chapterId = chapterId ?: 0,
    mangaId = mangaId ?: 0,
    mangaTitle = mangaTitle ?: "",
    chapterName = chapterName ?: "",
    state = DownloadState.fromString(state ?: "NOT_DOWNLOADED"),
    progress = progress ?: 0f,
    downloadedPages = downloadedPages ?: 0,
    totalPages = totalPages ?: 0
)

// ── History DTOs ──

data class HistoryResponse(
    @SerializedName("history") val history: List<HistoryDto>?
)

data class HistoryDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("mangaId") val mangaId: Int?,
    @SerializedName("mangaTitle") val mangaTitle: String?,
    @SerializedName("chapterId") val chapterId: Int?,
    @SerializedName("chapterTitle") val chapterTitle: String?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("lastReadAt") val lastReadAt: Long?
)

fun HistoryDto.toHistory(): History = History(
    id = id ?: 0,
    mangaId = mangaId ?: 0,
    mangaTitle = mangaTitle ?: "",
    chapterId = chapterId ?: 0,
    chapterTitle = chapterTitle ?: "",
    thumbnailUrl = thumbnailUrl ?: "",
    lastReadAt = lastReadAt ?: 0L
)
