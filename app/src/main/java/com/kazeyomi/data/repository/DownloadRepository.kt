package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.domain.model.Download
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getDownloads(): Result<List<Download>> = flow {
        try {
            val response = apiClient.getApi().getDownloads()
            emit(Result.success(response.downloads?.map { it.toDownload() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun enqueueDownload(chapterId: Int): Result<Unit> = flow {
        try {
            apiClient.getApi().enqueueDownload(chapterId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun cancelDownload(chapterId: Int): Result<Unit> = flow {
        try {
            apiClient.getApi().cancelDownload(chapterId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun clearDownloads(): Result<Unit> = flow {
        try {
            apiClient.getApi().clearDownloads()
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }
}
