package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getManga(mangaId: Int): Result<Manga> = runCatching {
        apiClient.getApi().getManga(mangaId).manga!!.toManga()
    }

    suspend fun getLibrary(categoryId: Int? = null): Result<List<Manga>> = runCatching {
        apiClient.getApi().getLibrary(categoryId).mangas?.mangas?.map { it.toManga() } ?: emptyList()
    }

    suspend fun addToLibrary(mangaId: Int): Result<Unit> = runCatching {
        apiClient.getApi().updateManga(mangaId, mapOf("inLibrary" to true))
    }

    suspend fun removeFromLibrary(mangaId: Int): Result<Unit> = runCatching {
        apiClient.getApi().updateManga(mangaId, mapOf("inLibrary" to false))
    }

    suspend fun updateManga(mangaId: Int, updates: Map<String, Any>): Result<Unit> = runCatching {
        apiClient.getApi().updateManga(mangaId, updates)
    }

    suspend fun fetchNewChapters(mangaId: Int): Result<Unit> = runCatching {
        apiClient.getApi().fetchNewChapters(mangaId)
    }

    suspend fun getChapters(mangaId: Int, order: String? = null): Result<List<Chapter>> = runCatching {
        apiClient.getApi().getChapters(mangaId, order).chapters?.map { it.toChapter() } ?: emptyList()
    }

    suspend fun getChapterPages(chapterId: Int): Result<List<ChapterPage>> = runCatching {
        apiClient.getApi().getChapterPages(chapterId).chapter?.pages?.map { it.toChapterPage() } ?: emptyList()
    }

    suspend fun markChapterAsRead(mangaId: Int, chapterId: Int, read: Boolean = true): Result<Unit> = runCatching {
        apiClient.getApi().markChapterAsRead(mangaId, chapterId, read)
    }

    suspend fun toggleBookmark(mangaId: Int, chapterId: Int, bookmarked: Boolean): Result<Unit> = runCatching {
        apiClient.getApi().toggleBookmark(mangaId, chapterId, bookmarked)
    }

    suspend fun setLastReadPage(mangaId: Int, chapterId: Int, page: Int): Result<Unit> = runCatching {
        apiClient.getApi().setReadHistory(mangaId, chapterId, page)
    }
}
