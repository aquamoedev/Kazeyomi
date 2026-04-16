package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.domain.model.History
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getHistory(): Result<List<History>> = flow {
        try {
            val response = apiClient.getApi().getHistory()
            emit(Result.success(response.history?.map { it.toHistory() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun deleteHistoryItem(historyId: Int): Result<Unit> = flow {
        try {
            apiClient.getApi().deleteHistoryItem(historyId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun clearHistory(): Result<Unit> = flow {
        try {
            apiClient.getApi().clearHistory()
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }
}
