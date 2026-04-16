package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.domain.model.Extension
import com.kazeyomi.domain.model.Manga
import com.kazeyomi.domain.model.Source
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BrowseRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getSources(installed: Boolean? = null): Result<List<Source>> = runCatching {
        apiClient.getApi().getSources(installed).sources?.map { it.toSource() } ?: emptyList()
    }

    suspend fun searchSource(sourceId: String, query: String): Result<List<Manga>> = runCatching {
        apiClient.getApi().searchSource(sourceId, query).mangas?.mangas?.map { it.toManga() } ?: emptyList()
    }

    suspend fun getPopularManga(sourceId: String, page: Int = 1): Result<List<Manga>> = runCatching {
        apiClient.getApi().getPopularManga(sourceId, page).mangas?.mangas?.map { it.toManga() } ?: emptyList()
    }

    suspend fun getLatestManga(sourceId: String, page: Int = 1): Result<List<Manga>> = runCatching {
        apiClient.getApi().getLatestManga(sourceId, page).mangas?.mangas?.map { it.toManga() } ?: emptyList()
    }

    suspend fun getExtensions(): Result<List<Extension>> = runCatching {
        apiClient.getApi().getExtensions().extensions?.map { it.toExtension() } ?: emptyList()
    }

    suspend fun installExtension(pkgName: String): Result<Unit> = runCatching {
        apiClient.getApi().installExtension(pkgName)
    }

    suspend fun uninstallExtension(pkgName: String): Result<Unit> = runCatching {
        apiClient.getApi().uninstallExtension(pkgName)
    }

    suspend fun updateExtension(pkgName: String): Result<Unit> = runCatching {
        apiClient.getApi().updateExtension(pkgName)
    }

    suspend fun globalSearch(query: String): Result<List<Manga>> = runCatching {
        apiClient.getApi().globalSearch(query).globalSearch?.map { it.toManga() } ?: emptyList()
    }
}
