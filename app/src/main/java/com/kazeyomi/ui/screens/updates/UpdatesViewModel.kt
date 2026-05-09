package com.kazeyomi.ui.screens.updates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.api.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpdateItem(val mangaId: Int, val mangaTitle: String, val chapterName: String, val timestamp: Long, val thumbnailUrl: String?)

data class UpdatesUiState(
    val updates: List<UpdateItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class UpdatesViewModel @Inject constructor(
    private val apiClient: ApiClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(UpdatesUiState())
    val uiState: StateFlow<UpdatesUiState> = _uiState.asStateFlow()

    fun loadUpdates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                if (apiClient.isConfigured()) {
                    val resp = apiClient.getApi().getUpdates()
                    val items = resp.updateList?.mapNotNull {
                        if (it.mangaId != null && it.mangaTitle != null && it.chapterName != null)
                            UpdateItem(it.mangaId, it.mangaTitle, it.chapterName, it.timestamp ?: 0L, it.thumbnailUrl)
                        else null
                    } ?: emptyList()
                    _uiState.update { it.copy(updates = items, isLoading = false) }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
