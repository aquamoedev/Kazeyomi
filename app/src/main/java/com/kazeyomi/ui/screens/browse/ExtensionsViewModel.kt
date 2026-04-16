package com.kazeyomi.ui.screens.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.BrowseRepository
import com.kazeyomi.domain.model.Extension
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExtensionsUiState(
    val extensions: List<Extension> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ExtensionsViewModel @Inject constructor(
    private val browseRepository: BrowseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExtensionsUiState())
    val uiState: StateFlow<ExtensionsUiState> = _uiState.asStateFlow()

    fun loadExtensions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = browseRepository.getExtensions()
            result.fold(
                onSuccess = { extensions ->
                    _uiState.update { it.copy(extensions = extensions, isLoading = false) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
            )
        }
    }

    fun installExtension(pkgName: String) {
        viewModelScope.launch {
            browseRepository.installExtension(pkgName)
            loadExtensions()
        }
    }

    fun uninstallExtension(pkgName: String) {
        viewModelScope.launch {
            browseRepository.uninstallExtension(pkgName)
            loadExtensions()
        }
    }

    fun updateExtension(pkgName: String) {
        viewModelScope.launch {
            browseRepository.updateExtension(pkgName)
            loadExtensions()
        }
    }
}
