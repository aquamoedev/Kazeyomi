package com.kazeyomi.data.api

import com.google.gson.annotations.SerializedName

data class MangaResponse(
    @SerializedName("manga")
    val manga: MangaDto?
)

data class MangaDto(
    val id: Int,
    val title: String,
    @SerializedName("title_aka")
    val titleAka: List<String>? = null,
    val author: String? = null,
    val artist: String? = null,
    val description: String? = null,
    val genre: List<String>? = null,
    val status: String = "UNKNOWN",
    val thumbnailUrl: String? = null,
    @SerializedName("inLibrary")
    val inLibrary: Boolean = false,
    @SerializedName("unreadCount")
    val unreadCount: Int = 0,
    @SerializedName("downloadCount")
    val downloadCount: Int = 0,
    @SerializedName("chapterCount")
    val chapterCount: Int = 0,
    @SerializedName("lastChapterRead")
    val lastChapterRead: Int = 0,
    @SerializedName("sourceId")
    val sourceId: String = "",
    @SerializedName("dateAdded")
    val dateAdded: Long? = null,
    @SerializedName("lastReadAt")
    val lastReadAt: Long? = null,
    @SerializedName("sortDescending")
    val sortDescending: Boolean = true,
    val categories: List<CategoryDto>? = null
) {
    fun toManga() = Manga(
        id = id,
        title = title,
        titleAka = titleAka,
        author = author,
        artist = artist,
        description = description,
        genre = genre,
        status = MangaStatus.valueOf(status),
        thumbnailUrl = thumbnailUrl,
        inLibrary = inLibrary,
        unreadCount = unreadCount,
        downloadCount = downloadCount,
        chapterCount = chapterCount,
        lastChapterRead = lastChapterRead,
        sourceId = sourceId,
        dateAdded = dateAdded,
        lastReadAt = lastReadAt,
        sortDescending = sortDescending,
        categories = categories?.map { it.toCategory() } ?: emptyList()
    )
}

data class MangasResponse(
    val mangas: MangasData?
)

data class MangasData(
    val mangas: List<MangaDto>? = null
)

data class CategoryDto(
    val id: Int = 0,
    val name: String = "",
    val order: Int = 0,
    val size: Int = 0,
    val default: Boolean = false,
    @SerializedName("isHidden")
    val isHidden: Boolean = false
) {
    fun toCategory() = Category(
        id = id,
        name = name,
        order = order,
        size = size,
        default = default,
        isHidden = isHidden
    )
}

data class ChapterDto(
    val id: Int,
    @SerializedName("mangaId")
    val mangaId: Int,
    @SerializedName("sourceId")
    val sourceId: String = "",
    val url: String = "",
    val title: String = "",
    val name: String = "",
    val volume: String = "",
    val chapter: String = "",
    val scanlator: String? = null,
    val read: Boolean = false,
    val bookmarked: Boolean = false,
    @SerializedName("lastPageRead")
    val lastPageRead: Int = 0,
    @SerializedName("pageCount")
    val pageCount: Int = 0,
    val status: String = "READY",
    @SerializedName("fetchAt")
    val fetchAt: Long = 0,
    @SerializedName("uploadDate")
    val uploadDate: Long = 0,
    @SerializedName("downloadState")
    val downloadState: String = "STATE_NON",
    @SerializedName("downloadProgress")
    val downloadProgress: Int = 0
) {
    fun toChapter() = Chapter(
        id = id,
        mangaId = mangaId,
        sourceId = sourceId,
        url = url,
        title = title,
        name = name,
        volume = volume,
        chapter = chapter,
        scanlator = scanlator,
        read = read,
        bookmarked = bookmarked,
        lastPageRead = lastPageRead,
        pageCount = pageCount,
        status = ChapterStatus.valueOf(status),
        fetchAt = fetchAt,
        uploadDate = uploadDate,
        downloadState = DownloadState.valueOf(downloadState),
        downloadProgress = downloadProgress
    )
}

data class ChaptersResponse(
    val chapters: List<ChapterDto>? = null
)

data class ChapterPagesResponse(
    val chapter: ChapterPagesDto?
)

data class ChapterPagesDto(
    val pages: List<ChapterPageDto>? = null
)

data class ChapterPageDto(
    val index: Int,
    val url: String,
    @SerializedName("pageNumber")
    val pageNumber: Int = 0,
    @SerializedName("imageUrl")
    val imageUrl: String = url
) {
    fun toChapterPage() = ChapterPage(
        index = index,
        url = url,
        pageNumber = pageNumber,
        imageUrl = imageUrl
    )
}
