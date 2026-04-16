package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.domain.model.ServerInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getServerInfo(): Result<ServerInfo> = runCatching {
        apiClient.getApi().getAboutServer().aboutServer?.toServerInfo() ?: ServerInfo()
    }

    fun isConfigured(): Boolean = apiClient.isConfigured()

    fun configure(baseUrl: String, username: String? = null, password: String? = null) {
        apiClient.configure(baseUrl, username, password)
    }

    fun getBaseUrl(): String = apiClient.getBaseUrl()
}
