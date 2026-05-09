package com.kazeyomi.ui.screens.reader
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.local.PreferencesManager
import com.kazeyomi.data.repository.MangaRepository
import com.kazeyomi.domain.model.Chapter
import com.kazeyomi.domain.model.ChapterPage
import com.kazeyomi.domain.model.ReaderSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReaderUiState(
    val pages: List<ChapterPage> = emptyList(), val currentPage: Int = 0, val chapterTitle: String = "",
    val hasPrevChapter: Boolean = false, val hasNextChapter: Boolean = false,
    val settings: ReaderSettings = ReaderSettings(), val isLoading: Boolean = false, val error: String? = null,
    val mangaId: Int = 0, val chapterId: Int = 0
)

@HiltViewModel
class ReaderViewModel @Inject constructor(private val mangaRepository: MangaRepository, private val prefsMgr: PreferencesManager) : ViewModel() {
    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()
    private var allChapters: List<Chapter> = emptyList()
    private var curIdx: Int = 0

    init { viewModelScope.launch { prefsMgr.readerSettings.collect { _uiState.update { s -> s.copy(settings = it) } } } }

    fun loadChapter(mangaId: Int, chapterId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, mangaId = mangaId, chapterId = chapterId) }
            try {
                allChapters = mangaRepository.getChapters(mangaId)
                curIdx = allChapters.indexOfFirst { it.id == chapterId }
                val pages = mangaRepository.getChapterPages(chapterId)
                val chapter = allChapters.find { it.id == chapterId }
                _uiState.update { it.copy(pages = pages, chapterTitle = chapter?.title ?: chapter?.name ?: "", currentPage = chapter?.lastPageRead ?: 0, hasPrevChapter = curIdx < allChapters.size - 1, hasNextChapter = curIdx > 0, isLoading = false) }
            } catch (e: Exception) { _uiState.update { it.copy(error = e.message, isLoading = false) } }
        }
    }
    fun prevChapter() { if (curIdx < allChapters.size - 1) { curIdx++; loadChapter(_uiState.value.mangaId, allChapters[curIdx].id) } }
    fun nextChapter() { if (curIdx > 0) { curIdx--; loadChapter(_uiState.value.mangaId, allChapters[curIdx].id) } }
    fun setPage(page: Int) { _uiState.update { it.copy(currentPage = page) }; saveProgress() }
    private fun saveProgress() {
        viewModelScope.launch {
            val s = _uiState.value
            if (s.mangaId > 0 && s.chapterId > 0) {
                try { mangaRepository.setLastReadPage(s.mangaId, s.chapterId, s.currentPage) } catch (_: Exception) {}
            }
        }
    }
}
