package com.kazeyomi.ui.screens.manga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.repository.DownloadRepository
import com.kazeyomi.data.repository.MangaRepository
import com.kazeyomi.domain.model.Chapter
import com.kazeyomi.domain.model.Manga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MangaDetailUiState(
    val manga: Manga? = null,
    val chapters: List<Chapter> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MangaDetailViewModel @Inject constructor(
    private val mangaRepository: MangaRepository,
    private val downloadRepository: DownloadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MangaDetailUiState())
    val uiState: StateFlow<MangaDetailUiState> = _uiState.asStateFlow()

    private var mangaId: Int = 0

    fun loadManga(id: Int) {
        mangaId = id
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val mangaResult = mangaRepository.getManga(id)
            mangaResult.fold(
                onSuccess = { manga ->
                    _uiState.update { it.copy(manga = manga) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                    return@launch
                }
            )

            loadChapters()
        }
    }

    fun fetchChapters() {
        viewModelScope.launch {
            mangaRepository.fetchNewChapters(mangaId)
            loadChapters()
        }
    }

    private suspend fun loadChapters() {
        val result = mangaRepository.getChapters(mangaId)
        result.fold(
            onSuccess = { chapters ->
                _uiState.update { it.copy(chapters = chapters, isLoading = false) }
            },
            onFailure = { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        )
    }

    fun setChapterSort(sortBy: String) {
        val sorted = when (sortBy) {
            "source" -> _uiState.value.chapters.sortedByDescending { it.sourceId }
            "number" -> _uiState.value.chapters.sortedByDescending { it.chapter.toFloatOrNull() ?: 0f }
            else -> _uiState.value.chapters
        }
        _uiState.update { it.copy(chapters = sorted) }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val manga = _uiState.value.manga ?: return@launch
            if (manga.inLibrary) {
                mangaRepository.removeFromLibrary(manga.id)
            } else {
                mangaRepository.addToLibrary(manga.id)
            }
            loadManga(mangaId)
        }
    }

    fun toggleChapterRead(chapter: Chapter) {
        viewModelScope.launch {
            mangaRepository.markChapterAsRead(mangaId, chapter.id, !chapter.read)
            loadChapters()
        }
    }

    fun downloadChapter(chapterId: Int) {
        viewModelScope.launch {
            downloadRepository.enqueueDownload(chapterId)
        }
    }
}
