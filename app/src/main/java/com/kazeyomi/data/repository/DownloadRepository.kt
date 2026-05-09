package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.data.api.toDownload
import com.kazeyomi.data.api.UpdateItem
import com.kazeyomi.domain.model.Download
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getDownloads(): List<Download> {
        val response = apiClient.getApi().getDownloads()
        return response.downloads?.map { it.toDownload() } ?: emptyList()
    }

    suspend fun enqueueDownload(chapterId: Int) {
        apiClient.getApi().enqueueDownload(chapterId)
    }

    suspend fun cancelDownload(chapterId: Int) {
        apiClient.getApi().cancelDownload(chapterId)
    }

    suspend fun clearDownloads() {
        apiClient.getApi().clearDownloads()
    }

    suspend fun getUpdates(since: Long? = null): List<UpdateItem> {
        val response = apiClient.getApi().getUpdates(since)
        return response.updateList?.map { dto ->
            UpdateItem(
                mangaId = dto.mangaId ?: 0,
                mangaTitle = dto.mangaTitle ?: "",
                thumbnailUrl = dto.thumbnailUrl ?: "",
                chapterId = dto.chapterId ?: 0,
                chapterName = dto.chapterName ?: "",
                timestamp = dto.timestamp ?: 0L,
                sourceId = dto.sourceId ?: ""
            )
        } ?: emptyList()
    }
}
