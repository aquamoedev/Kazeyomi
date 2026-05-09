package com.kazeyomi.ui.screens.about
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.ServerRepository
import com.kazeyomi.domain.model.ServerInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AboutUiState(val serverInfo: ServerInfo? = null, val isLoading: Boolean = false, val error: String? = null)

@HiltViewModel
class AboutViewModel @Inject constructor(private val serverRepository: ServerRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AboutUiState())
    val uiState: StateFlow<AboutUiState> = _uiState.asStateFlow()
    fun load() {
        if (!serverRepository.isConfigured()) { _uiState.update { it.copy(error = "Server not configured") }; return }
        viewModelScope.launch { _uiState.update { it.copy(isLoading = true, error = null) }; try { _uiState.update { it.copy(serverInfo = serverRepository.getServerInfo(), isLoading = false) } } catch (e: Exception) { _uiState.update { it.copy(error = e.message, isLoading = false) } } }
    }
}
