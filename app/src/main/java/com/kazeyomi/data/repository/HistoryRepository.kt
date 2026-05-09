package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.data.api.toHistory
import com.kazeyomi.domain.model.History
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getHistory(): List<History> {
        val response = apiClient.getApi().getHistory()
        return response.history?.map { it.toHistory() } ?: emptyList()
    }

    suspend fun deleteHistory(historyId: Int) {
        apiClient.getApi().deleteHistory(historyId)
    }

    suspend fun clearHistory() {
        apiClient.getApi().clearHistory()
    }
}
