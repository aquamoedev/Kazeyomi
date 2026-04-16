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
            launch { preferencesManager.serverUrl.collect { _uiState.update { it.copy(serverUrl = it.serverUrl) } } }
            launch { preferencesManager.themeMode.collect { _uiState.update { it.copy(themeMode = it.themeMode) } } }
            launch { preferencesManager.dynamicColor.collect { _uiState.update { it.copy(dynamicColor = it.dynamicColor) } } }
            launch { preferencesManager.language.collect { _uiState.update { it.copy(language = it.language) } } }
            launch { preferencesManager.libraryDisplayMode.collect { _uiState.update { it.copy(libraryDisplayMode = it.libraryDisplayMode) } } }
            launch { preferencesManager.autoUpdateLibrary.collect { _uiState.update { it.copy(autoUpdateLibrary = it.autoUpdateLibrary) } } }
            launch { preferencesManager.readerMode.collect { _uiState.update { it.copy(readerMode = it.readerMode) } } }
            launch { preferencesManager.readerDirection.collect { _uiState.update { it.copy(readerDirection = it.readerDirection) } } }
            launch { preferencesManager.readerShowPageNum.collect { _uiState.update { it.copy(readerShowPageNum = it.readerShowPageNum) } } }
            launch { preferencesManager.readerVolumeKeys.collect { _uiState.update { it.copy(readerVolumeKeys = it.readerVolumeKeys) } } }
            launch { preferencesManager.readerKeepScreen.collect { _uiState.update { it.copy(readerKeepScreen = it.readerKeepScreen) } } }
            launch { preferencesManager.readerAutoMark.collect { _uiState.update { it.copy(readerAutoMark = it.readerAutoMark) } } }
            launch { preferencesManager.downloadAsCbz.collect { _uiState.update { it.copy(downloadAsCbz = it.downloadAsCbz) } } }
            launch { preferencesManager.downloadChapters.collect { _uiState.update { it.copy(downloadChapters = it.downloadChapters) } } }
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
