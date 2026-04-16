package com.kazeyomi.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val serverUrl: String = "",
    val themeMode: String = "system",
    val dynamicColor: Boolean = true,
    val language: String = "en",
    val libraryDisplayMode: String = "grid",
    val librarySortBy: String = "title",
    val autoUpdateLibrary: Boolean = false,
    val readerMode: String = "VERTICAL",
    val readerDirection: String = "LEFT_TO_RIGHT",
    val readerShowPageNum: Boolean = true,
    val readerVolumeKeys: Boolean = true,
    val readerKeepScreen: Boolean = true,
    val readerAutoMark: Boolean = true,
    val downloadAsCbz: Boolean = true,
    val downloadChapters: Int = 3
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            launch { preferencesManager.serverUrl.collect { value -> _uiState.update { it.copy(serverUrl = value) } } }
            launch { preferencesManager.themeMode.collect { value -> _uiState.update { it.copy(themeMode = value) } } }
            launch { preferencesManager.dynamicColor.collect { value -> _uiState.update { it.copy(dynamicColor = value) } } }
            launch { preferencesManager.language.collect { value -> _uiState.update { it.copy(language = value) } } }
            launch { preferencesManager.libraryDisplayMode.collect { value -> _uiState.update { it.copy(libraryDisplayMode = value) } } }
            launch { preferencesManager.autoUpdateLibrary.collect { value -> _uiState.update { it.copy(autoUpdateLibrary = value) } } }
            launch { preferencesManager.readerMode.collect { value -> _uiState.update { it.copy(readerMode = value) } } }
            launch { preferencesManager.readerDirection.collect { value -> _uiState.update { it.copy(readerDirection = value) } } }
            launch { preferencesManager.readerShowPageNum.collect { value -> _uiState.update { it.copy(readerShowPageNum = value) } } }
            launch { preferencesManager.readerVolumeKeys.collect { value -> _uiState.update { it.copy(readerVolumeKeys = value) } } }
            launch { preferencesManager.readerKeepScreen.collect { value -> _uiState.update { it.copy(readerKeepScreen = value) } } }
            launch { preferencesManager.readerAutoMark.collect { value -> _uiState.update { it.copy(readerAutoMark = value) } } }
            launch { preferencesManager.downloadAsCbz.collect { value -> _uiState.update { it.copy(downloadAsCbz = value) } } }
            launch { preferencesManager.downloadChapters.collect { value -> _uiState.update { it.copy(downloadChapters = value) } } }
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch { preferencesManager.setThemeMode(mode) }
    }

    fun setDynamicColor(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setDynamicColor(enabled) }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch { preferencesManager.setLanguage(lang) }
    }

    fun setLibraryDisplayMode(mode: String) {
        viewModelScope.launch { preferencesManager.setLibraryDisplayMode(mode) }
    }

    fun setAutoUpdateLibrary(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setAutoUpdateLibrary(enabled) }
    }

    fun setReaderMode(mode: String) {
        viewModelScope.launch { preferencesManager.setReaderMode(mode) }
    }

    fun setReaderDirection(direction: String) {
        viewModelScope.launch { preferencesManager.setReaderDirection(direction) }
    }

    fun setReaderShowPageNum(show: Boolean) {
        viewModelScope.launch { preferencesManager.setReaderShowPageNum(show) }
    }

    fun setReaderVolumeKeys(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setReaderVolumeKeys(enabled) }
    }

    fun setReaderKeepScreen(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setReaderKeepScreen(enabled) }
    }

    fun setReaderAutoMark(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setReaderAutoMark(enabled) }
    }

    fun setDownloadAsCbz(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setDownloadAsCbz(enabled) }
    }

    fun setDownloadChapters(amount: Int) {
        viewModelScope.launch { preferencesManager.setDownloadChapters(amount) }
    }
}
