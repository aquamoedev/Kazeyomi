package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.domain.model.Extension
import com.kazeyomi.domain.model.Source
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BrowseRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getSources(installed: Boolean? = null): Result<List<Source>> = flow {
        try {
            val response = apiClient.getApi().getSources(installed)
            emit(Result.success(response.sources?.map { it.toSource() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun searchSource(sourceId: String, query: String): Result<List<com.kazeyomi.domain.model.Manga>> = flow {
        try {
            val response = apiClient.getApi().searchSource(sourceId, query)
            emit(Result.success(response.mangas?.mangas?.map { it.toManga() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun getPopularManga(sourceId: String, page: Int = 1): Result<List<com.kazeyomi.domain.model.Manga>> = flow {
        try {
            val response = apiClient.getApi().getPopularManga(sourceId, page)
            emit(Result.success(response.mangas?.mangas?.map { it.toManga() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun getLatestManga(sourceId: String, page: Int = 1): Result<List<com.kazeyomi.domain.model.Manga>> = flow {
        try {
            val response = apiClient.getApi().getLatestManga(sourceId, page)
            emit(Result.success(response.mangas?.mangas?.map { it.toManga() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun getExtensions(): Result<List<Extension>> = flow {
        try {
            val response = apiClient.getApi().getExtensions()
            emit(Result.success(response.extensions?.map { it.toExtension() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun installExtension(pkgName: String): Result<Unit> = flow {
        try {
            apiClient.getApi().installExtension(pkgName)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun uninstallExtension(pkgName: String): Result<Unit> = flow {
        try {
            apiClient.getApi().uninstallExtension(pkgName)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun updateExtension(pkgName: String): Result<Unit> = flow {
        try {
            apiClient.getApi().updateExtension(pkgName)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun globalSearch(query: String): Result<List<com.kazeyomi.domain.model.Manga>> = flow {
        try {
            val response = apiClient.getApi().globalSearch(query)
            emit(Result.success(response.globalSearch?.map { it.toManga() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }
}
