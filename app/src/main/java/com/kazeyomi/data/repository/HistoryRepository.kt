package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.domain.model.History
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getHistory(): Result<List<History>> = runCatching {
        apiClient.getApi().getHistory().history?.map { it.toHistory() } ?: emptyList()
    }

    suspend fun deleteHistoryItem(historyId: Int): Result<Unit> = runCatching {
        apiClient.getApi().deleteHistoryItem(historyId)
    }

    suspend fun clearHistory(): Result<Unit> = runCatching {
        apiClient.getApi().clearHistory()
    }
}
