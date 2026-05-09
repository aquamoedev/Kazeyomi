package com.kazeyomi.ui.screens.manga
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.MangaRepository
import com.kazeyomi.data.repository.DownloadRepository
import com.kazeyomi.domain.model.Chapter
import com.kazeyomi.domain.model.Manga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MangaDetailUiState(val manga: Manga? = null, val chapters: List<Chapter> = emptyList(), val isLoading: Boolean = false, val error: String? = null)

@HiltViewModel
class MangaDetailViewModel @Inject constructor(private val mangaRepository: MangaRepository, private val downloadRepository: DownloadRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MangaDetailUiState())
    val uiState: StateFlow<MangaDetailUiState> = _uiState.asStateFlow()
    private var mangaId: Int = 0

    fun loadManga(id: Int) {
        mangaId = id
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val manga = mangaRepository.getManga(id)
                val chapters = mangaRepository.getChapters(id)
                _uiState.update { it.copy(manga = manga, chapters = chapters, isLoading = false) }
            } catch (e: Exception) { _uiState.update { it.copy(error = e.message, isLoading = false) } }
        }
    }
    fun fetchChapters() { viewModelScope.launch { try { mangaRepository.fetchNewChapters(mangaId); loadManga(mangaId) } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
    fun toggleFavorite() {
        viewModelScope.launch {
            val m = _uiState.value.manga ?: return@launch
            try {
                if (m.inLibrary) mangaRepository.removeFromLibrary(m.id) else mangaRepository.addToLibrary(m.id)
                loadManga(mangaId)
            } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } }
        }
    }
    fun toggleChapterRead(chapter: Chapter) { viewModelScope.launch { try { mangaRepository.markChapterAsRead(mangaId, chapter.id, !chapter.read); loadManga(mangaId) } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
    fun toggleBookmark(chapter: Chapter) { viewModelScope.launch { try { mangaRepository.toggleBookmark(mangaId, chapter.id, !chapter.bookmarked); loadManga(mangaId) } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
    fun downloadChapter(chapterId: Int) { viewModelScope.launch { try { downloadRepository.enqueueDownload(chapterId) } catch (e: Exception) { _uiState.update { it.copy(error = e.message) } } } }
}
