package com.kazeyomi.ui.screens.settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.api.ApiException
import com.kazeyomi.data.local.PreferencesManager
import com.kazeyomi.data.repository.ServerRepository
import com.kazeyomi.domain.model.ServerInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ServerSetupUiState(val isConnecting: Boolean = false, val isConnected: Boolean = false, val serverInfo: ServerInfo? = null, val error: String? = null)

@HiltViewModel
class ServerSetupViewModel @Inject constructor(private val serverRepository: ServerRepository, private val prefs: PreferencesManager) : ViewModel() {
    private val _uiState = MutableStateFlow(ServerSetupUiState())
    val uiState: StateFlow<ServerSetupUiState> = _uiState.asStateFlow()

    fun connect(url: String, username: String?, password: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isConnecting = true, error = null) }
            try {
                serverRepository.configure(url, username, password)
                prefs.setServerUrl(url)
                if (username != null && password != null) prefs.setServerCredentials(username, password)
                val info = serverRepository.getServerInfo()
                _uiState.update { it.copy(isConnecting = false, isConnected = true, serverInfo = info) }
            } catch (e: Exception) {
                val msg = when (e) { is ApiException -> when (e.code) { 401 -> "Authentication failed"; 404 -> "Server not found"; else -> "Error ${e.code}: ${e.message}" }; else -> e.message ?: "Connection failed" }
                _uiState.update { it.copy(isConnecting = false, error = msg) }
            }
        }
    }
}
