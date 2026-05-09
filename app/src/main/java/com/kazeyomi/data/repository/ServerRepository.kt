package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.data.api.toServerInfo
import com.kazeyomi.domain.model.ServerInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getServerInfo(): ServerInfo {
        return apiClient.getApi().getAboutServer().toServerInfo()
    }

    fun configure(baseUrl: String, username: String? = null, password: String? = null) {
        apiClient.configure(baseUrl, username, password)
    }

    fun isConfigured(): Boolean = apiClient.isConfigured()
    fun getBaseUrl(): String = apiClient.getBaseUrl()
}
