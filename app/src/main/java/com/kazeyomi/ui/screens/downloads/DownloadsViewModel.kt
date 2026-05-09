package com.kazeyomi.ui.screens.downloads
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.DownloadRepository
import com.kazeyomi.domain.model.Download
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DownloadsUiState(val downloads: List<Download> = emptyList(), val isLoading: Boolean = false, val error: String? = null)

@HiltViewModel
class DownloadsViewModel @Inject constructor(private val downloadRepository: DownloadRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DownloadsUiState())
    val uiState: StateFlow<DownloadsUiState> = _uiState.asStateFlow()
    fun load() { viewModelScope.launch { _uiState.update { it.copy(isLoading = true, error = null) }; try { _uiState.update { it.copy(downloads = downloadRepository.getDownloads(), isLoading = false) } } catch (e: Exception) { _uiState.update { it.copy(error = e.message, isLoading = false) } } } }
    fun cancel(id: Int) { viewModelScope.launch { try { downloadRepository.cancelDownload(id); load() } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
    fun clear() { viewModelScope.launch { try { downloadRepository.clearDownloads(); load() } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
}
