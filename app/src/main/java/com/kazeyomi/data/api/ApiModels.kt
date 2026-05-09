package com.kazeyomi.data.api

import com.google.gson.annotations.SerializedName
import com.kazeyomi.domain.model.*

// ── Manga DTOs ──

data class MangaDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("author") val author: String?,
    @SerializedName("artist") val artist: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("genre") val genre: List<String>?,
    @SerializedName("status") val status: String?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("inLibrary") val inLibrary: Boolean?,
    @SerializedName("unreadCount") val unreadCount: Int?,
    @SerializedName("downloadCount") val downloadCount: Int?,
    @SerializedName("chapterCount") val chapterCount: Int?,
    @SerializedName("sourceId") val sourceId: String?,
    @SerializedName("realUrl") val realUrl: String?,
    @SerializedName("freshData") val freshData: Boolean?,
    @SerializedName("updateCount") val updateCount: Int?
)

fun MangaDto.toManga(): Manga = Manga(
    id = id,
    title = title ?: "",
    author = author ?: "",
    artist = artist ?: "",
    description = description ?: "",
    genre = genre ?: emptyList(),
    status = MangaStatus.fromString(status ?: "UNKNOWN"),
    thumbnailUrl = thumbnailUrl ?: "",
    inLibrary = inLibrary ?: false,
    unreadCount = unreadCount ?: 0,
    downloadCount = downloadCount ?: 0,
    chapterCount = chapterCount ?: 0,
    sourceId = sourceId ?: "",
    updateCount = updateCount ?: 0
)

data class MangaResponse(
    @SerializedName("manga") val manga: MangaDto?
)

data class MangasResponse(
    @SerializedName("mangaList") val mangaList: List<MangaDto>?,
    @SerializedName("hasNextPage") val hasNextPage: Boolean?
)

// ── Chapter DTOs ──

data class ChapterDto(
    @SerializedName("id") val id: Int,
    @SerializedName("mangaId") val mangaId: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("chapterNumber") val chapterNumber: Float?,
    @SerializedName("pageCount") val pageCount: Int?,
    @SerializedName("uploadDate") val uploadDate: Long?,
    @SerializedName("read") val read: Boolean?,
    @SerializedName("bookmarked") val bookmarked: Boolean?,
    @SerializedName("lastPageRead") val lastPageRead: Int?,
    @SerializedName("downloaded") val downloaded: Boolean?,
    @SerializedName("scanlator") val scanlator: String?,
    @SerializedName("chapterCount") val chapterCount: Int?
)

fun ChapterDto.toChapter(): Chapter = Chapter(
    id = id,
    mangaId = mangaId ?: 0,
    title = name ?: "",
    name = name ?: "",
    chapter = chapterNumber ?: 0f,
    pageCount = pageCount ?: 0,
    uploadDate = uploadDate ?: 0L,
    read = read ?: false,
    bookmarked = bookmarked ?: false,
    lastPageRead = lastPageRead ?: 0,
    downloadState = if (downloaded == true) DownloadState.DOWNLOADED else DownloadState.NOT_DOWNLOADED,
    scanlator = scanlator ?: ""
)

data class ChaptersResponse(
    @SerializedName("chapterList") val chapterList: List<ChapterDto>?
)

data class ChapterPagesResponse(
    @SerializedName("pageList") val pageList: List<ChapterPageDto>?
)

data class ChapterPageDto(
    @SerializedName("index") val index: Int,
    @SerializedName("pageNumber") val pageNumber: Int?,
    @SerializedName("chapterId") val chapterId: Int?
)

fun ChapterPageDto.toChapterPage(baseUrl: String = ""): ChapterPage = ChapterPage(
    index = index,
    url = if (baseUrl.isNotEmpty()) "$baseUrl/api/v1/chapter/${chapterId ?: 0}/page/$index" else "",
    pageNumber = pageNumber ?: (index + 1),
    imageUrl = ""
)

// ── Category DTOs ──

data class CategoryDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("order") val order: Int?,
    @SerializedName("size") val size: Int?,
    @SerializedName("default") val default: Boolean?,
    @SerializedName("includeInUpdate") val includeInUpdate: Boolean?,
    @SerializedName("includeInDownload") val includeInDownload: Boolean?
)

fun CategoryDto.toCategory(): Category = Category(
    id = id ?: 0,
    name = name ?: "",
    order = order ?: 0,
    size = size ?: 0,
    default = default ?: false,
    isHidden = false
)

// ── Source DTOs ──

data class SourceDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("iconUrl") val iconUrl: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("lang") val lang: String?,
    @SerializedName("isConfigurable") val isConfigurable: Boolean?,
    @SerializedName("isNsfw") val isNsfw: Boolean?,
    @SerializedName("version") val version: String?,
    @SerializedName("hasUpdate") val hasUpdate: Boolean?
)

fun SourceDto.toSource(): Source = Source(
    id = id ?: "",
    name = name ?: "",
    iconUrl = iconUrl ?: "",
    displayName = displayName ?: (name ?: ""),
    language = lang ?: "",
    isConfigurable = isConfigurable ?: false,
    isInstalled = true,
    isNsfw = isNsfw ?: false,
    version = version ?: ""
)

data class SourcesResponse(
    @SerializedName("sources") val sources: List<SourceDto>?
)

// ── Global Search DTOs ──

data class GlobalSearchResponse(
    @SerializedName("searchResults") val searchResults: List<SearchResultDto>?
)

data class SearchResultDto(
    @SerializedName("manga") val manga: MangaDto?,
    @SerializedName("sourceName") val sourceName: String?,
    @SerializedName("sourceId") val sourceId: String?
)

// ── Updates DTOs ──

data class UpdatesResponse(
    @SerializedName("updateList") val updateList: List<UpdateDto>?
)

data class UpdateDto(
    @SerializedName("mangaId") val mangaId: Int?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("mangaTitle") val mangaTitle: String?,
    @SerializedName("chapterId") val chapterId: Int?,
    @SerializedName("chapterName") val chapterName: String?,
    @SerializedName("timestamp") val timestamp: Long?,
    @SerializedName("sourceId") val sourceId: String?
)

data class UpdateItem(
    val mangaId: Int,
    val mangaTitle: String,
    val thumbnailUrl: String,
    val chapterId: Int,
    val chapterName: String,
    val timestamp: Long,
    val sourceId: String
)

// ── About/Server DTOs ──

data class AboutServerResponse(
    @SerializedName("version") val version: String?,
    @SerializedName("versionName") val versionName: String?,
    @SerializedName("apiVersion") val apiVersion: String?,
    @SerializedName("dataFolder") val dataFolder: String?,
    @SerializedName("buildType") val buildType: String?,
    @SerializedName("buildTime") val buildTime: Long?,
    @SerializedName("name") val name: String?
)

fun AboutServerResponse.toServerInfo(): ServerInfo = ServerInfo(
    version = version ?: "",
    versionName = versionName ?: "",
    apiVersion = (apiVersion?.toIntOrNull() ?: 0),
    dataFolder = dataFolder ?: ""
)
