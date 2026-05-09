package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.data.api.*
import com.kazeyomi.domain.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BrowseRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getSources(installed: Boolean? = null): List<Source> {
        val response = apiClient.getApi().getSources(installed)
        return response.sources?.map { it.toSource() } ?: emptyList()
    }

    suspend fun searchSource(sourceId: String, query: String): List<Manga> {
        val response = apiClient.getApi().searchSource(sourceId, query)
        return response.mangaList?.map { it.toManga() } ?: emptyList()
    }

    suspend fun getPopularManga(sourceId: String, page: Int = 1): List<Manga> {
        val response = apiClient.getApi().getPopularManga(sourceId, page)
        return response.mangaList?.map { it.toManga() } ?: emptyList()
    }

    suspend fun getLatestManga(sourceId: String, page: Int = 1): List<Manga> {
        val response = apiClient.getApi().getLatestManga(sourceId, page)
        return response.mangaList?.map { it.toManga() } ?: emptyList()
    }

    suspend fun globalSearch(query: String): List<Manga> {
        val response = apiClient.getApi().globalSearch(query)
        return response.searchResults?.mapNotNull { it.manga?.toManga() } ?: emptyList()
    }

    suspend fun getExtensions(): List<Extension> {
        val response = apiClient.getApi().getExtensions()
        return response.extensions?.map { it.toExtension() } ?: emptyList()
    }

    suspend fun installExtension(pkgName: String) {
        apiClient.getApi().installExtension(pkgName)
    }

    suspend fun uninstallExtension(pkgName: String) {
        apiClient.getApi().uninstallExtension(pkgName)
    }

    suspend fun updateExtension(pkgName: String) {
        apiClient.getApi().updateExtension(pkgName)
    }

    suspend fun getSourcePreferences(sourceId: String): Map<String, String> {
        return apiClient.getApi().getSourcePreferences(sourceId) ?: emptyMap()
    }

    suspend fun saveSourcePreferences(sourceId: String, preferences: Map<String, String>) {
        apiClient.getApi().saveSourcePreferences(sourceId, preferences)
    }
}
