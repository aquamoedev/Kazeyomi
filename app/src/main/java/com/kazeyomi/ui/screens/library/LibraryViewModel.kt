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

    init { loadCategories() }

    fun loadLibrary() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val mangas = libraryRepository.getLibrary(_uiState.value.selectedCategory?.id)
                _uiState.update { it.copy(mangas = sortMangas(mangas, _uiState.value.sortBy, _uiState.value.sortDescending), isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun refreshLibrary() = loadLibrary()

    fun selectCategory(category: Category?) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadLibrary()
    }

    fun setSortBy(sortBy: String) {
        _uiState.update { it.copy(sortBy = sortBy).let { s -> s.copy(mangas = sortMangas(s.mangas, sortBy, s.sortDescending)) } }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(categories = libraryRepository.getCategories()) }
            } catch (_: Exception) {}
        }
    }

    private fun sortMangas(mangas: List<Manga>, sortBy: String, desc: Boolean): List<Manga> {
        val sorted = when (sortBy) {
            "title" -> mangas.sortedBy { it.title.lowercase() }
            "lastRead" -> mangas.sortedBy { it.lastReadAt ?: 0L }
            "dateAdded" -> mangas.sortedBy { it.dateAdded ?: 0L }
            else -> mangas
        }
        return if (desc) sorted.reversed() else sorted
    }
}
