package com.kazeyomi.ui.screens.browse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.BrowseRepository
import com.kazeyomi.domain.model.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BrowseUiState(val sources: List<Source> = emptyList(), val isLoading: Boolean = false, val error: String? = null)

@HiltViewModel
class BrowseViewModel @Inject constructor(private val browseRepository: BrowseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState.asStateFlow()
    fun loadSources() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try { _uiState.update { it.copy(sources = browseRepository.getSources(true), isLoading = false) } }
            catch (e: Exception) { _uiState.update { it.copy(error = e.message, isLoading = false) } }
        }
    }
}
