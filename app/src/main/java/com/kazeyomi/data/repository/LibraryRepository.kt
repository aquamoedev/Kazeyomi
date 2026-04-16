package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.domain.model.Category
import com.kazeyomi.domain.model.CategoryDto
import com.kazeyomi.domain.model.Manga
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getLibrary(categoryId: Int? = null): Result<List<Manga>> = flow {
        try {
            val response = apiClient.getApi().getLibrary(categoryId)
            emit(Result.success(response.mangas?.mangas?.map { it.toManga() } ?: emptyList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun getCategories(): Result<List<Category>> = flow {
        try {
            val categories = apiClient.getApi().getCategories()
            emit(Result.success(categories.map { it.toCategory() }))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun createCategory(name: String): Result<Category> = flow {
        try {
            val dto = CategoryDto(name = name)
            val created = apiClient.getApi().createCategory(dto)
            emit(Result.success(created.toCategory()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun updateCategory(categoryId: Int, name: String, order: Int, isHidden: Boolean): Result<Unit> = flow {
        try {
            val dto = CategoryDto(id = categoryId, name = name, order = order, isHidden = isHidden)
            apiClient.getApi().updateCategory(categoryId, dto)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun deleteCategory(categoryId: Int): Result<Unit> = flow {
        try {
            apiClient.getApi().deleteCategory(categoryId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }

    suspend fun addMangaToCategory(mangaId: Int, categoryIds: List<Int>): Result<Unit> = flow {
        try {
            apiClient.getApi().addMangaToCategory(mangaId, categoryIds)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.let { flow -> kotlinx.coroutines.flow.first(flow) }
}
