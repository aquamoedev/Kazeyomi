package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.data.api.CategoryDto
import com.kazeyomi.domain.model.Category
import com.kazeyomi.domain.model.Manga
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getLibrary(categoryId: Int? = null): Result<List<Manga>> = runCatching {
        apiClient.getApi().getLibrary(categoryId).mangas?.mangas?.map { it.toManga() } ?: emptyList()
    }

    suspend fun getCategories(): Result<List<Category>> = runCatching {
        apiClient.getApi().getCategories().map { it.toCategory() }
    }

    suspend fun createCategory(name: String): Result<Category> = runCatching {
        val dto = CategoryDto(name = name)
        apiClient.getApi().createCategory(dto).toCategory()
    }

    suspend fun updateCategory(categoryId: Int, name: String, order: Int, isHidden: Boolean): Result<Unit> = runCatching {
        val dto = CategoryDto(id = categoryId, name = name, order = order, isHidden = isHidden)
        apiClient.getApi().updateCategory(categoryId, dto)
    }

    suspend fun deleteCategory(categoryId: Int): Result<Unit> = runCatching {
        apiClient.getApi().deleteCategory(categoryId)
    }

    suspend fun addMangaToCategory(mangaId: Int, categoryIds: List<Int>): Result<Unit> = runCatching {
        apiClient.getApi().addMangaToCategory(mangaId, categoryIds)
    }
}
