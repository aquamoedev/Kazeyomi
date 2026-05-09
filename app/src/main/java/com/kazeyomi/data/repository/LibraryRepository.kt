package com.kazeyomi.data.repository

import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.data.api.*
import com.kazeyomi.domain.model.Category
import com.kazeyomi.domain.model.Manga
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getLibrary(categoryId: Int? = null): List<Manga> {
        val response = apiClient.getApi().getLibrary(categoryId)
        return response.mangaList?.map { it.toManga() } ?: emptyList()
    }

    suspend fun getCategories(): List<Category> {
        return apiClient.getApi().getCategories().map { it.toCategory() }
    }

    suspend fun createCategory(name: String) {
        val dto = CategoryDto(id = null, name = name, order = 0, size = null, default = false, includeInUpdate = false, includeInDownload = false)
        apiClient.getApi().createCategory(dto)
    }

    suspend fun updateCategory(categoryId: Int, name: String) {
        val dto = CategoryDto(id = categoryId, name = name, order = 0, size = null, default = false, includeInUpdate = false, includeInDownload = false)
        apiClient.getApi().updateCategory(categoryId, dto)
    }

    suspend fun deleteCategory(categoryId: Int) {
        apiClient.getApi().deleteCategory(categoryId)
    }

    suspend fun addMangaToCategory(mangaId: Int, categoryIds: List<Int>) {
        apiClient.getApi().addMangaToCategory(mangaId, categoryIds)
    }
}
