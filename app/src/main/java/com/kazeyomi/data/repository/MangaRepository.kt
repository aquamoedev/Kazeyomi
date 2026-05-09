package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.data.api.toChapter
import com.kazeyomi.data.api.toChapterPage
import com.kazeyomi.data.api.toManga
import com.kazeyomi.domain.model.Chapter
import com.kazeyomi.domain.model.ChapterPage
import com.kazeyomi.domain.model.Manga
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getManga(mangaId: Int): Manga {
        val response = apiClient.getApi().getManga(mangaId)
        return response.manga!!.toManga()
    }

    suspend fun getChapters(mangaId: Int, order: String? = null): List<Chapter> {
        val response = apiClient.getApi().getChapters(mangaId, order)
        return response.chapterList?.map { it.toChapter() } ?: emptyList()
    }

    suspend fun getChapterPages(chapterId: Int): List<ChapterPage> {
        val response = apiClient.getApi().getChapterPages(chapterId)
        return response.pageList?.map { it.toChapterPage() } ?: emptyList()
    }

    suspend fun markChapterAsRead(mangaId: Int, chapterId: Int, read: Boolean = true) {
        apiClient.getApi().markChapterAsRead(mangaId, chapterId, read)
    }

    suspend fun toggleBookmark(mangaId: Int, chapterId: Int, bookmark: Boolean) {
        apiClient.getApi().toggleBookmark(mangaId, chapterId, bookmark)
    }

    suspend fun updateManga(mangaId: Int, update: Map<String, Any>) {
        apiClient.getApi().updateManga(mangaId, update)
    }

    suspend fun fetchNewChapters(mangaId: Int) {
        apiClient.getApi().fetchNewChapters(mangaId)
    }

    suspend fun addMangaToCategory(mangaId: Int, categoryIds: List<Int>) {
        apiClient.getApi().addMangaToCategory(mangaId, categoryIds)
    }

    suspend fun setReadHistory(mangaId: Int, chapterId: Int, lastPage: Int) {
        apiClient.getApi().setReadHistory(mangaId, chapterId, lastPage)
    }

    suspend fun addToLibrary(mangaId: Int) {
        apiClient.getApi().updateManga(mangaId, mapOf("inLibrary" to true))
    }

    suspend fun removeFromLibrary(mangaId: Int) {
        apiClient.getApi().updateManga(mangaId, mapOf("inLibrary" to false))
    }

    suspend fun setLastReadPage(mangaId: Int, chapterId: Int, page: Int) {
        apiClient.getApi().setReadHistory(mangaId, chapterId, page)
    }
}
