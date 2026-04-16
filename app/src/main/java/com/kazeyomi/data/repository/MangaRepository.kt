package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getManga(mangaId: Int): Result<Manga> = flow {
        try {
            val response = apiClient.getApi().getManga(mangaId)
            emit(Result.success(response.manga!!.toManga()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun getLibrary(categoryId: Int? = null): Result<List<Manga>> = flow {
        try {
            val response = apiClient.getApi().getLibrary(categoryId)
            emit(Result.success(response.mangas?.mangas?.map { it.toManga() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun addToLibrary(mangaId: Int): Result<Unit> = flow {
        try {
            apiClient.getApi().updateManga(mangaId, mapOf("inLibrary" to true))
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun removeFromLibrary(mangaId: Int): Result<Unit> = flow {
        try {
            apiClient.getApi().updateManga(mangaId, mapOf("inLibrary" to false))
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun updateManga(mangaId: Int, updates: Map<String, Any>): Result<Unit> = flow {
        try {
            apiClient.getApi().updateManga(mangaId, updates)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun fetchNewChapters(mangaId: Int): Result<Unit> = flow {
        try {
            apiClient.getApi().fetchNewChapters(mangaId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun getChapters(mangaId: Int, order: String? = null): Result<List<Chapter>> = flow {
        try {
            val response = apiClient.getApi().getChapters(mangaId, order)
            emit(Result.success(response.chapters?.map { it.toChapter() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun getChapterPages(chapterId: Int): Result<List<ChapterPage>> = flow {
        try {
            val response = apiClient.getApi().getChapterPages(chapterId)
            emit(Result.success(response.chapter?.pages?.map { it.toChapterPage() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun markChapterAsRead(mangaId: Int, chapterId: Int, read: Boolean = true): Result<Unit> = flow {
        try {
            apiClient.getApi().markChapterAsRead(mangaId, chapterId, read)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun toggleBookmark(mangaId: Int, chapterId: Int, bookmarked: Boolean): Result<Unit> = flow {
        try {
            apiClient.getApi().toggleBookmark(mangaId, chapterId, bookmarked)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun setLastReadPage(mangaId: Int, chapterId: Int, page: Int): Result<Unit> = flow {
        try {
            apiClient.getApi().setReadHistory(mangaId, chapterId, page)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }
}
