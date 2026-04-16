package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.domain.model.Download
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getDownloads(): Result<List<Download>> = runCatching {
        apiClient.getApi().getDownloads().downloads?.map { it.toDownload() } ?: emptyList()
    }

    suspend fun enqueueDownload(chapterId: Int): Result<Unit> = runCatching {
        apiClient.getApi().enqueueDownload(chapterId)
    }

    suspend fun cancelDownload(chapterId: Int): Result<Unit> = runCatching {
        apiClient.getApi().cancelDownload(chapterId)
    }

    suspend fun clearDownloads(): Result<Unit> = runCatching {
        apiClient.getApi().clearDownloads()
    }
}
