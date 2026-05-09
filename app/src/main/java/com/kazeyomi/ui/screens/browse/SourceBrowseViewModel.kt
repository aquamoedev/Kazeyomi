package com.kazeyomi.ui.screens.browse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.BrowseRepository
import com.kazeyomi.domain.model.Manga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SourceBrowseUiState(
    val popular: List<Manga> = emptyList(), val popularLoading: Boolean = false, val popularError: String? = null, val popularPage: Int = 1,
    val latest: List<Manga> = emptyList(), val latestLoading: Boolean = false, val latestError: String? = null, val latestPage: Int = 1,
    val searchResults: List<Manga> = emptyList(), val searchLoading: Boolean = false, val searchError: String? = null
)

@HiltViewModel
class SourceBrowseViewModel @Inject constructor(private val browseRepository: BrowseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SourceBrowseUiState())
    val uiState: StateFlow<SourceBrowseUiState> = _uiState.asStateFlow()
    private var sourceId: String = ""

    fun init(id: String) { sourceId = id; loadPopular(); loadLatest() }
    fun loadPopular() { viewModelScope.launch { _uiState.update { it.copy(popularLoading = true, popularError = null) }; try { _uiState.update { it.copy(popular = browseRepository.getPopularManga(sourceId, 1), popularLoading = false, popularPage = 1) } } catch (e: Exception) { _uiState.update { it.copy(popularLoading = false, popularError = e.message) } } } }
    fun loadLatest() { viewModelScope.launch { _uiState.update { it.copy(latestLoading = true, latestError = null) }; try { _uiState.update { it.copy(latest = browseRepository.getLatestManga(sourceId, 1), latestLoading = false, latestPage = 1) } } catch (e: Exception) { _uiState.update { it.copy(latestLoading = false, latestError = e.message) } } } }
    fun loadMorePopular() { viewModelScope.launch { val s = _uiState.value; if (s.popularLoading) return@launch; val page = s.popularPage + 1; _uiState.update { it.copy(popularLoading = true) }; try { _uiState.update { it.copy(popular = s.popular + browseRepository.getPopularManga(sourceId, page), popularLoading = false, popularPage = page) } } catch (e: Exception) { _uiState.update { it.copy(popularLoading = false) } } } }
    fun loadMoreLatest() { viewModelScope.launch { val s = _uiState.value; if (s.latestLoading) return@launch; val page = s.latestPage + 1; _uiState.update { it.copy(latestLoading = true) }; try { _uiState.update { it.copy(latest = s.latest + browseRepository.getLatestManga(sourceId, page), latestLoading = false, latestPage = page) } } catch (e: Exception) { _uiState.update { it.copy(latestLoading = false) } } } }
    fun search(query: String) { viewModelScope.launch { _uiState.update { it.copy(searchLoading = true, searchError = null) }; try { _uiState.update { it.copy(searchResults = browseRepository.searchSource(sourceId, query), searchLoading = false) } } catch (e: Exception) { _uiState.update { it.copy(searchLoading = false, searchError = e.message) } } } }
}
