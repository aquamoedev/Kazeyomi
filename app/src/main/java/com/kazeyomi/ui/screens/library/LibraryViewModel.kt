package com.kazeyomi.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.LibraryRepository
import com.kazeyomi.domain.model.Category
import com.kazeyomi.domain.model.Manga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LibraryUiState(
    val mangas: List<Manga> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val sortBy: String = "title",
    val sortDescending: Boolean = true
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadLibrary() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = libraryRepository.getLibrary(_uiState.value.selectedCategory?.id)
            result.fold(
                onSuccess = { mangas ->
                    val sorted = sortMangas(mangas, _uiState.value.sortBy, _uiState.value.sortDescending)
                    _uiState.update { it.copy(mangas = sorted, isLoading = false) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
            )
        }
    }

    fun refreshLibrary() {
        loadLibrary()
    }

    fun selectCategory(category: Category?) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadLibrary()
    }

    fun setSortBy(sortBy: String) {
        _uiState.update { it.copy(sortBy = sortBy) }
        val sorted = sortMangas(_uiState.value.mangas, sortBy, _uiState.value.sortDescending)
        _uiState.update { it.copy(mangas = sorted) }
    }

    fun toggleSortDirection() {
        val newDesc = !_uiState.value.sortDescending
        _uiState.update { it.copy(sortDescending = newDesc) }
        val sorted = sortMangas(_uiState.value.mangas, _uiState.value.sortBy, newDesc)
        _uiState.update { it.copy(mangas = sorted) }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val result = libraryRepository.getCategories()
            result.fold(
                onSuccess = { categories ->
                    _uiState.update { it.copy(categories = categories) }
                },
                onFailure = {}
            )
        }
    }

    private fun sortMangas(mangas: List<Manga>, sortBy: String, descending: Boolean): List<Manga> {
        val sorted = when (sortBy) {
            "title" -> mangas.sortedBy { it.title.lowercase() }
            "lastRead" -> mangas.sortedBy { it.lastReadAt ?: 0L }
            "dateAdded" -> mangas.sortedBy { it.dateAdded ?: 0L }
            else -> mangas
        }
        return if (descending) sorted.reversed() else sorted
    }
}
