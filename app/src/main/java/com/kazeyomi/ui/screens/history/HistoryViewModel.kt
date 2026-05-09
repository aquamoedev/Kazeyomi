package com.kazeyomi.ui.screens.history
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.HistoryRepository
import com.kazeyomi.domain.model.History
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(val items: List<History> = emptyList(), val isLoading: Boolean = false, val error: String? = null)

@HiltViewModel
class HistoryViewModel @Inject constructor(private val historyRepository: HistoryRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    fun load() { viewModelScope.launch { _uiState.update { it.copy(isLoading = true, error = null) }; try { _uiState.update { it.copy(items = historyRepository.getHistory(), isLoading = false) } } catch (e: Exception) { _uiState.update { it.copy(error = e.message, isLoading = false) } } } }
    fun delete(id: Int) { viewModelScope.launch { try { historyRepository.deleteHistory(id); load() } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
    fun clear() { viewModelScope.launch { try { historyRepository.clearHistory(); load() } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
}
