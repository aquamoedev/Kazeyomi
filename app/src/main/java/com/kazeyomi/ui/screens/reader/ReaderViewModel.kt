package com.kazeyomi.ui.screens.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.local.PreferencesManager
import com.kazeyomi.data.repository.MangaRepository
import com.kazeyomi.domain.model.Chapter
import com.kazeyomi.domain.model.ChapterPage
import com.kazeyomi.domain.model.ReaderSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReaderUiState(
    val mangaId: Int = 0,
    val chapterId: Int = 0,
    val chapterTitle: String = "",
    val pages: List<ChapterPage> = emptyList(),
    val currentPage: Int = 0,
    val settings: ReaderSettings = ReaderSettings(),
    val hasPreviousChapter: Boolean = false,
    val hasNextChapter: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val mangaRepository: MangaRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    private var allChapters: List<Chapter> = emptyList()
    private var currentChapterIndex: Int = 0

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val settings = preferencesManager.getReaderSettings().first()
            _uiState.update { it.copy(settings = settings) }
        }
    }

    fun loadChapter(mangaId: Int, chapterId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, mangaId = mangaId, chapterId = chapterId) }

            val chaptersResult = mangaRepository.getChapters(mangaId)
            chaptersResult.fold(
                onSuccess = { chapters ->
                    allChapters = chapters
                    currentChapterIndex = chapters.indexOfFirst { it.id == chapterId }
                },
                onFailure = {}
            )

            val pagesResult = mangaRepository.getChapterPages(chapterId)
            pagesResult.fold(
                onSuccess = { pages ->
                    val chapter = allChapters.find { it.id == chapterId }
                    _uiState.update {
                        it.copy(
                            pages = pages,
                            chapterTitle = chapter?.title ?: "",
                            currentPage = chapter?.lastPageRead ?: 0,
                            hasPreviousChapter = currentChapterIndex < allChapters.size - 1,
                            hasNextChapter = currentChapterIndex > 0,
                            isLoading = false
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
            )
        }
    }

    fun loadPreviousChapter() {
        if (currentChapterIndex < allChapters.size - 1) {
            currentChapterIndex++
            val chapter = allChapters[currentChapterIndex]
            loadChapter(_uiState.value.mangaId, chapter.id)
        }
    }

    fun loadNextChapter() {
        if (currentChapterIndex > 0) {
            currentChapterIndex--
            val chapter = allChapters[currentChapterIndex]
            loadChapter(_uiState.value.mangaId, chapter.id)
        }
    }

    fun setCurrentPage(page: Int) {
        _uiState.update { it.copy(currentPage = page) }
        saveProgress()
    }

    private fun saveProgress() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.mangaId > 0 && state.chapterId > 0) {
                mangaRepository.setLastReadPage(state.mangaId, state.chapterId, state.currentPage)
                
                if (state.settings.autoMarkAsRead && state.currentPage >= state.pages.size * state.settings.autoMarkPageThreshold / 100) {
                    mangaRepository.markChapterAsRead(state.mangaId, state.chapterId, true)
                }
            }
        }
    }

    fun setReadingMode(mode: com.kazeyomi.domain.model.ReadingMode) {
        viewModelScope.launch {
            preferencesManager.setReaderMode(mode.name)
            _uiState.update { it.copy(settings = it.settings.copy(mode = mode)) }
        }
    }

    fun setReadingDirection(direction: com.kazeyomi.domain.model.ReadingDirection) {
        viewModelScope.launch {
            preferencesManager.setReaderDirection(direction.name)
            _uiState.update { it.copy(settings = it.settings.copy(direction = direction)) }
        }
    }
}
