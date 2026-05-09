package com.kazeyomi.ui.screens.browse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.BrowseRepository
import com.kazeyomi.domain.model.Extension
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExtensionsUiState(val extensions: List<Extension> = emptyList(), val isLoading: Boolean = false, val error: String? = null)

@HiltViewModel
class ExtensionsViewModel @Inject constructor(private val browseRepository: BrowseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ExtensionsUiState())
    val uiState: StateFlow<ExtensionsUiState> = _uiState.asStateFlow()
    fun loadExtensions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try { _uiState.update { it.copy(extensions = browseRepository.getExtensions(), isLoading = false) } }
            catch (e: Exception) { _uiState.update { it.copy(error = e.message, isLoading = false) } }
        }
    }
    fun install(pkgName: String) { viewModelScope.launch { try { browseRepository.installExtension(pkgName); loadExtensions() } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
    fun uninstall(pkgName: String) { viewModelScope.launch { try { browseRepository.uninstallExtension(pkgName); loadExtensions() } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
    fun update(pkgName: String) { viewModelScope.launch { try { browseRepository.updateExtension(pkgName); loadExtensions() } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
}
