package com.kazeyomi.ui.screens.browse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.BrowseRepository
import com.kazeyomi.domain.model.Manga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GlobalSearchUiState(val results: List<Manga> = emptyList(), val isLoading: Boolean = false, val error: String? = null)

@HiltViewModel
class GlobalSearchViewModel @Inject constructor(private val browseRepository: BrowseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GlobalSearchUiState())
    val uiState: StateFlow<GlobalSearchUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    fun search(query: String) {
        searchJob?.cancel()
        if (query.length < 2) { _uiState.update { it.copy(results = emptyList(), isLoading = false) }; return }
        searchJob = viewModelScope.launch {
            delay(400)
            _uiState.update { it.copy(isLoading = true, error = null) }
            try { _uiState.update { it.copy(results = browseRepository.globalSearch(query), isLoading = false) } }
            catch (e: Exception) { _uiState.update { it.copy(error = e.message, isLoading = false) } }
        }
    }
    fun clear() { searchJob?.cancel(); _uiState.update { it.copy(results = emptyList(), error = null) } }
}
