package com.kazeyomi.ui.screens.browse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.BrowseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SourcePreferencesUiState(val isLoading: Boolean = false, val error: String? = null, val preferences: Map<String, String> = emptyMap(), val saved: Boolean = false)

@HiltViewModel
class SourcePreferencesViewModel @Inject constructor(private val browseRepository: BrowseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SourcePreferencesUiState())
    val uiState: StateFlow<SourcePreferencesUiState> = _uiState.asStateFlow()
    private var sourceId: String = ""

    fun load(sourceId: String) { this.sourceId = sourceId; viewModelScope.launch { _uiState.update { it.copy(isLoading = true, error = null) }; try { _uiState.update { it.copy(preferences = browseRepository.getSourcePreferences(sourceId), isLoading = false) } } catch (e: Exception) { _uiState.update { it.copy(isLoading = false, error = e.message) } } } }
    fun update(key: String, value: String) { _uiState.update { it.copy(preferences = it.preferences + (key to value), saved = false) } }
    fun save() { viewModelScope.launch { try { browseRepository.saveSourcePreferences(sourceId, _uiState.value.preferences); _uiState.update { it.copy(saved = true) } } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
}
