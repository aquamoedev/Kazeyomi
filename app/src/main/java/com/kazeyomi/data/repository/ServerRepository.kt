package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.domain.model.ServerInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getServerInfo(): Result<ServerInfo> = flow {
        try {
            val response = apiClient.getApi().getAboutServer()
            emit(Result.success(response.aboutServer?.toServerInfo() ?: ServerInfo()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    fun isConfigured(): Boolean = apiClient.isConfigured()

    fun configure(baseUrl: String, username: String? = null, password: String? = null) {
        apiClient.configure(baseUrl, username, password)
    }

    fun getBaseUrl(): String = apiClient.getBaseUrl()
}
