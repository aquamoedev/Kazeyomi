package com.kazeyomi.data.api

import com.google.gson.annotations.SerializedName

data class SourceDto(
    val id: String = "",
    val name: String = "",
    val iconUrl: String? = null,
    val websiteBaseUrl: String = "",
    val displayName: String = "",
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
    val categories: List<String>? = null
) {
    fun toSource() = Source(
        id = id,
        name = name,
        iconUrl = iconUrl,
        websiteBaseUrl = websiteBaseUrl,
        displayName = displayName,
        language = language,
        isConfigurable = isConfigurable,
        isInstalled = isInstalled,
        isNsfw = isNsfw,
        hasCloudFlare = hasCloudFlare,
        version = version,
        requiresStudio = requiresStudio,
        categories = categories ?: emptyList()
    )
}

data class SourcesResponse(
    val sources: List<SourceDto>? = null
)

data class ExtensionDto(
    val name: String = "",
    val version: String = "",
    val pkgName: String = "",
    val iconUrl: String? = null,
    val lang: String = "",
    val type: String = "UNKNOWN",
    @SerializedName("isInstalled")
    val isInstalled: Boolean = false,
    @SerializedName("isObsolete")
    val isObsolete: Boolean = false,
    @SerializedName("hasUpdate")
    val hasUpdate: Boolean = false,
    @SerializedName("installButton")
    val installButton: String = "INSTALLED",
    val sources: List<String>? = null
) {
    fun toExtension() = Extension(
        name = name,
        version = version,
        pkgName = pkgName,
        iconUrl = iconUrl,
        lang = lang,
        type = ExtensionType.valueOf(type),
        isInstalled = isInstalled,
        isObsolete = isObsolete,
        hasUpdate = hasUpdate,
        installButton = InstallButton.valueOf(installButton),
        sources = sources ?: emptyList()
    )
}

data class ExtensionsResponse(
    val extensions: List<ExtensionDto>? = null
)

data class DownloadDto(
    val chapterId: Int = 0,
    val mangaId: Int = 0,
    val mangaTitle: String = "",
    val chapterName: String = "",
    val state: String = "STATE_NON",
    val progress: Int = 0,
    @SerializedName("downloadedPages")
    val downloadedPages: Int = 0,
    @SerializedName("totalPages")
    val totalPages: Int = 0,
    val retryCount: Int = 0
) {
    fun toDownload() = Download(
        chapterId = chapterId,
        mangaId = mangaId,
        mangaTitle = mangaTitle,
        chapterName = chapterName,
        state = DownloadState.valueOf(state),
        progress = progress,
        downloadedPages = downloadedPages,
        totalPages = totalPages,
        retryCount = retryCount
    )
}

data class DownloadsResponse(
    val downloads: List<DownloadDto>? = null
)

data class HistoryDto(
    val id: Int = 0,
    @SerializedName("mangaId")
    val mangaId: Int = 0,
    @SerializedName("mangaTitle")
    val mangaTitle: String = "",
    @SerializedName("chapterId")
    val chapterId: Int = 0,
    @SerializedName("chapterTitle")
    val chapterTitle: String = "",
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String? = null,
    @SerializedName("lastReadAt")
    val lastReadAt: Long = 0,
    val duration: Long = 0
) {
    fun toHistory() = History(
        id = id,
        mangaId = mangaId,
        mangaTitle = mangaTitle,
        chapterId = chapterId,
        chapterTitle = chapterTitle,
        thumbnailUrl = thumbnailUrl,
        lastReadAt = lastReadAt,
        duration = duration
    )
}

data class HistoryResponse(
    val history: List<HistoryDto>? = null
)

data class ServerInfoDto(
    val version: String = "",
    @SerializedName("versionName")
    val versionName: String = "",
    @SerializedName("apiVersion")
    val apiVersion: Int = 0,
    @SerializedName("databaseVersion")
    val databaseVersion: Int = 0,
    @SerializedName("dataFolder")
    val dataFolder: String = "",
    @SerializedName("iconUrl")
    val iconUrl: String? = null
) {
    fun toServerInfo() = ServerInfo(
        version = version,
        versionName = versionName,
        apiVersion = apiVersion,
        databaseVersion = databaseVersion,
        dataFolder = dataFolder,
        iconUrl = iconUrl
    )
}

data class AboutServerResponse(
    val aboutServer: ServerInfoDto?
)

data class SearchResultDto(
    val id: Int = 0,
    val title: String = "",
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String? = null,
    @SerializedName("sourceId")
    val sourceId: String = ""
) {
    fun toManga() = Manga(
        id = id,
        title = title,
        thumbnailUrl = thumbnailUrl,
        sourceId = sourceId
    )
}

data class GlobalSearchResponse(
    val globalSearch: List<SearchResultDto>? = null
)

data class UpdateDto(
    val mangaId: Int = 0,
    @SerializedName("mangaTitle")
    val mangaTitle: String = "",
    val chapterId: Int = 0,
    val chapterName: String = "",
    @SerializedName("chapterTime")
    val chapterTime: Long = 0,
    @SerializedName("unread")
    val unread: Boolean = true,
    @SerializedName("downloadState")
    val downloadState: String = "STATE_NON",
    val thumbnailUrl: String? = null
)

data class UpdatesResponse(
    val updates: List<UpdateDto>? = null
)
