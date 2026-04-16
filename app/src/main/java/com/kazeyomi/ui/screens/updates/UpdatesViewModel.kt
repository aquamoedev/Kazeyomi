package com.kazeyomi.ui.screens.updates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.data.api.UpdateDto
import com.kazeyomi.domain.model.DownloadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpdateUiModel(
    val mangaId: Int,
    val mangaTitle: String,
    val chapterId: Int,
    val chapterName: String,
    val chapterTime: Long,
    val isUnread: Boolean,
    val downloadState: DownloadState,
    val thumbnailUrl: String?
)

data class UpdatesUiState(
    val updates: List<UpdateUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class UpdatesViewModel @Inject constructor(
    private val apiClient: ApiClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpdatesUiState())
    val uiState: StateFlow<UpdatesUiState> = _uiState.asStateFlow()

    fun loadUpdates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = apiClient.getApi().getUpdates()
                val updates = response.updates?.map { dto: UpdateDto ->
                    UpdateUiModel(
                        mangaId = dto.mangaId,
                        mangaTitle = dto.mangaTitle,
                        chapterId = dto.chapterId,
                        chapterName = dto.chapterName,
                        chapterTime = dto.chapterTime,
                        isUnread = dto.unread,
                        downloadState = DownloadState.valueOf(dto.downloadState),
                        thumbnailUrl = dto.thumbnailUrl
                    )
                } ?: emptyList()
                _uiState.update { it.copy(updates = updates, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun markAsRead(chapterId: Int) {
        viewModelScope.launch {
            try {
                apiClient.getApi().markChapterAsRead(0, chapterId, true)
                _uiState.update { state ->
                    state.copy(
                        updates = state.updates.map {
                            if (it.chapterId == chapterId) it.copy(isUnread = false) else it
                        }
                    )
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
