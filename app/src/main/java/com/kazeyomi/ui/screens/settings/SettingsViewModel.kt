package com.kazeyomi.ui.screens.settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazeyomi.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(val serverUrl: String = "", val themeMode: String = "system", val dynamicColor: Boolean = true, val readerMode: String = "VERTICAL", val autoUpdateLibrary: Boolean = false)

data class AppearanceState(val themeIndex: Int = 0, val isTrueBlack: Boolean = false, val gridCoverWidth: Int = 140)
data class BrowseState(val showNsfw: Boolean = false)
data class DownloadsState(val maxConcurrent: Int = 3, val deleteAfterRead: Boolean = false)
data class LibraryState(val hideEmptyCategories: Boolean = false, val defaultSort: String = "title")

@HiltViewModel
class SettingsViewModel @Inject constructor(private val prefs: PreferencesManager) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()
    private val _appearance = MutableStateFlow(AppearanceState())
    val appearance: StateFlow<AppearanceState> = _appearance.asStateFlow()
    private val _browse = MutableStateFlow(BrowseState())
    val browse: StateFlow<BrowseState> = _browse.asStateFlow()
    private val _downloads = MutableStateFlow(DownloadsState())
    val downloads: StateFlow<DownloadsState> = _downloads.asStateFlow()
    private val _library = MutableStateFlow(LibraryState())
    val library: StateFlow<LibraryState> = _library.asStateFlow()

    init { viewModelScope.launch { launch { prefs.serverUrl.collect { _state.update { s -> s.copy(serverUrl = it) } } }; launch { prefs.themeMode.collect { _state.update { s -> s.copy(themeMode = it) } } }; launch { prefs.readerMode.collect { _state.update { s -> s.copy(readerMode = it) } } } } }

    fun setTheme(index: Int) { _appearance.update { it.copy(themeIndex = index) }; val mode = listOf("system","light","dark")[index.coerceIn(0..2)]; viewModelScope.launch { prefs.setThemeMode(mode) } }
    fun setTrueBlack(v: Boolean) { _appearance.update { it.copy(isTrueBlack = v) }; viewModelScope.launch { prefs.setIsTrueBlack(v) } }
    fun setGridCoverWidth(w: Int) { _appearance.update { it.copy(gridCoverWidth = w) }; viewModelScope.launch { prefs.setGridCoverWidth(w) } }
    fun setShowNsfw(v: Boolean) { _browse.update { it.copy(showNsfw = v) }; viewModelScope.launch { prefs.setShowNsfw(v) } }
    fun setMaxConcurrentDownloads(m: Int) { _downloads.update { it.copy(maxConcurrent = m) }; viewModelScope.launch { prefs.setMaxConcurrentDownloads(m) } }
    fun setDeleteAfterRead(v: Boolean) { _downloads.update { it.copy(deleteAfterRead = v) } }
    fun setHideEmptyCategories(v: Boolean) { _library.update { it.copy(hideEmptyCategories = v) }; viewModelScope.launch { prefs.setHideEmptyCategories(v) } }
    fun setDefaultLibrarySort(s: String) { _library.update { it.copy(defaultSort = s) }; viewModelScope.launch { prefs.setDefaultLibrarySort(s) } }
    fun setAutoUpdateLibrary(v: Boolean) { viewModelScope.launch { prefs.setAutoUpdateLibrary(v); _state.update { it.copy(autoUpdateLibrary = v) } } }
}
