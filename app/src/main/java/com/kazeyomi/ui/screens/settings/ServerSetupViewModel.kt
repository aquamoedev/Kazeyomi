package com.kazeyomi.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.local.PreferencesManager
import com.kazeyomi.data.repository.ServerRepository
import com.kazeyomi.domain.model.ServerInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ServerSetupUiState(
    val isConnecting: Boolean = false,
    val isConnected: Boolean = false,
    val serverInfo: ServerInfo? = null,
    val error: String? = null
)

@HiltViewModel
class ServerSetupViewModel @Inject constructor(
    private val serverRepository: ServerRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServerSetupUiState())
    val uiState: StateFlow<ServerSetupUiState> = _uiState.asStateFlow()

    fun connect(serverUrl: String, username: String?, password: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isConnecting = true, error = null) }

            try {
                serverRepository.configure(serverUrl, username, password)
                preferencesManager.setServerUrl(serverUrl)
                if (username != null && password != null) {
                    preferencesManager.setServerCredentials(username, password)
                }

                val result = serverRepository.getServerInfo()
                result.fold(
                    onSuccess = { info ->
                        _uiState.update {
                            it.copy(
                                isConnecting = false,
                                isConnected = true,
                                serverInfo = info
                            )
                        }
                    },
                    onFailure = { e ->
                        _uiState.update {
                            it.copy(
                                isConnecting = false,
                                error = "Connection failed: ${e.message}"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isConnecting = false,
                        error = "Connection failed: ${e.message}"
                    )
                }
            }
        }
    }
}
