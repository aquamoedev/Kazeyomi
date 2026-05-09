package com.kazeyomi.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.kazeyomi.domain.model.ReaderSettings
import com.kazeyomi.domain.model.ReadingDirection
import com.kazeyomi.domain.model.ReadingMode
import com.kazeyomi.domain.model.TapZones
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "kazeyomi_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val KEY_SERVER_URL = stringPreferencesKey("server_url")
        private val KEY_SERVER_USERNAME = stringPreferencesKey("server_username")
        private val KEY_SERVER_PASSWORD = stringPreferencesKey("server_password")
        private val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        private val KEY_DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_LIBRARY_DISPLAY_MODE = stringPreferencesKey("library_display_mode")
        private val KEY_LIBRARY_SORT_BY = stringPreferencesKey("library_sort_by")
        private val KEY_AUTO_UPDATE_LIBRARY = booleanPreferencesKey("auto_update_library")
        private val KEY_HIDE_EMPTY_CATEGORIES = booleanPreferencesKey("hide_empty_categories")
        private val KEY_READER_MODE = stringPreferencesKey("reader_mode")
        private val KEY_READER_DIRECTION = stringPreferencesKey("reader_direction")
        private val KEY_READER_SHOW_PAGE_NUM = booleanPreferencesKey("reader_show_page_num")
        private val KEY_READER_VOLUME_KEYS = booleanPreferencesKey("reader_volume_keys")
        private val KEY_READER_KEEP_SCREEN = booleanPreferencesKey("reader_keep_screen")
        private val KEY_READER_AUTO_MARK = booleanPreferencesKey("reader_auto_mark")
        private val KEY_READER_PRELOAD = intPreferencesKey("reader_preload")
        private val KEY_DOWNLOAD_AS_CBZ = booleanPreferencesKey("download_as_cbz")
        private val KEY_MAX_CONCURRENT_DOWNLOADS = intPreferencesKey("max_concurrent_downloads")
        private val KEY_SHOW_NSFW = booleanPreferencesKey("show_nsfw")
        private val KEY_AUTO_BACKUP = booleanPreferencesKey("auto_backup")
        private val KEY_AUTO_BACKUP_INTERVAL = intPreferencesKey("auto_backup_interval")
        private val KEY_GRID_COVER_WIDTH = intPreferencesKey("grid_cover_width")
        private val KEY_IS_TRUE_BLACK = booleanPreferencesKey("is_true_black")
        private val KEY_DEFAULT_LIBRARY_SORT = stringPreferencesKey("default_library_sort")
        private val KEY_DOWNLOAD_CHAPTERS = intPreferencesKey("download_chapters")
    }

    val serverUrl: Flow<String> get() = dataStore.data.map { it[KEY_SERVER_URL] ?: "" }
    val themeMode: Flow<String> get() = dataStore.data.map { it[KEY_THEME_MODE] ?: "system" }
    val dynamicColor: Flow<Boolean> get() = dataStore.data.map { it[KEY_DYNAMIC_COLOR] ?: true }
    val language: Flow<String> get() = dataStore.data.map { it[KEY_LANGUAGE] ?: "en" }
    val libraryDisplayMode: Flow<String> get() = dataStore.data.map { it[KEY_LIBRARY_DISPLAY_MODE] ?: "grid" }
    val librarySortBy: Flow<String> get() = dataStore.data.map { it[KEY_LIBRARY_SORT_BY] ?: "title" }
    val autoUpdateLibrary: Flow<Boolean> get() = dataStore.data.map { it[KEY_AUTO_UPDATE_LIBRARY] ?: false }
    val hideEmptyCategories: Flow<Boolean> get() = dataStore.data.map { it[KEY_HIDE_EMPTY_CATEGORIES] ?: false }
    val readerMode: Flow<String> get() = dataStore.data.map { it[KEY_READER_MODE] ?: "VERTICAL" }
    val readerDirection: Flow<String> get() = dataStore.data.map { it[KEY_READER_DIRECTION] ?: "LEFT_TO_RIGHT" }
    val readerShowPageNum: Flow<Boolean> get() = dataStore.data.map { it[KEY_READER_SHOW_PAGE_NUM] ?: true }
    val readerVolumeKeys: Flow<Boolean> get() = dataStore.data.map { it[KEY_READER_VOLUME_KEYS] ?: false }
    val readerKeepScreen: Flow<Boolean> get() = dataStore.data.map { it[KEY_READER_KEEP_SCREEN] ?: true }
    val readerAutoMark: Flow<Boolean> get() = dataStore.data.map { it[KEY_READER_AUTO_MARK] ?: true }
    val readerPreload: Flow<Int> get() = dataStore.data.map { it[KEY_READER_PRELOAD] ?: 10 }
    val downloadAsCbz: Flow<Boolean> get() = dataStore.data.map { it[KEY_DOWNLOAD_AS_CBZ] ?: false }
    val maxConcurrentDownloads: Flow<Int> get() = dataStore.data.map { it[KEY_MAX_CONCURRENT_DOWNLOADS] ?: 3 }
    val showNsfw: Flow<Boolean> get() = dataStore.data.map { it[KEY_SHOW_NSFW] ?: false }
    val autoBackupEnabled: Flow<Boolean> get() = dataStore.data.map { it[KEY_AUTO_BACKUP] ?: false }
    val autoBackupInterval: Flow<Int> get() = dataStore.data.map { it[KEY_AUTO_BACKUP_INTERVAL] ?: 24 }
    val gridCoverWidth: Flow<Int> get() = dataStore.data.map { it[KEY_GRID_COVER_WIDTH] ?: 140 }
    val isTrueBlack: Flow<Boolean> get() = dataStore.data.map { it[KEY_IS_TRUE_BLACK] ?: false }
    val defaultLibrarySort: Flow<String> get() = dataStore.data.map { it[KEY_DEFAULT_LIBRARY_SORT] ?: "title" }
    val downloadChapters: Flow<Int> get() = dataStore.data.map { it[KEY_DOWNLOAD_CHAPTERS] ?: 3 }

    val readerSettings: Flow<ReaderSettings> get() = dataStore.data.map { prefs ->
        ReaderSettings(
            mode = try { ReadingMode.valueOf(prefs[KEY_READER_MODE] ?: "VERTICAL") } catch (_: Exception) { ReadingMode.VERTICAL },
            direction = try { ReadingDirection.valueOf(prefs[KEY_READER_DIRECTION] ?: "LEFT_TO_RIGHT") } catch (_: Exception) { ReadingDirection.LEFT_TO_RIGHT },
            showPageNumber = prefs[KEY_READER_SHOW_PAGE_NUM] ?: true,
            tapZones = TapZones.STANDARD,
            volumeKeys = prefs[KEY_READER_VOLUME_KEYS] ?: false,
            keepScreenOn = prefs[KEY_READER_KEEP_SCREEN] ?: true,
            autoMarkAsRead = prefs[KEY_READER_AUTO_MARK] ?: true,
            preloadAmount = prefs[KEY_READER_PRELOAD] ?: 10
        )
    }

    suspend fun setServerUrl(url: String) { dataStore.edit { it[KEY_SERVER_URL] = url } }
    suspend fun setServerCredentials(username: String?, password: String?) { dataStore.edit { it[KEY_SERVER_USERNAME] = username ?: ""; it[KEY_SERVER_PASSWORD] = password ?: "" } }
    suspend fun setThemeMode(mode: String) { dataStore.edit { it[KEY_THEME_MODE] = mode } }
    suspend fun setDynamicColor(enabled: Boolean) { dataStore.edit { it[KEY_DYNAMIC_COLOR] = enabled } }
    suspend fun setLanguage(lang: String) { dataStore.edit { it[KEY_LANGUAGE] = lang } }
    suspend fun setLibraryDisplayMode(mode: String) { dataStore.edit { it[KEY_LIBRARY_DISPLAY_MODE] = mode } }
    suspend fun setLibrarySortBy(sort: String) { dataStore.edit { it[KEY_LIBRARY_SORT_BY] = sort } }
    suspend fun setAutoUpdateLibrary(enabled: Boolean) { dataStore.edit { it[KEY_AUTO_UPDATE_LIBRARY] = enabled } }
    suspend fun setHideEmptyCategories(enabled: Boolean) { dataStore.edit { it[KEY_HIDE_EMPTY_CATEGORIES] = enabled } }
    suspend fun setReaderMode(mode: String) { dataStore.edit { it[KEY_READER_MODE] = mode } }
    suspend fun setReaderDirection(direction: String) { dataStore.edit { it[KEY_READER_DIRECTION] = direction } }
    suspend fun setReaderShowPageNum(show: Boolean) { dataStore.edit { it[KEY_READER_SHOW_PAGE_NUM] = show } }
    suspend fun setReaderVolumeKeys(enabled: Boolean) { dataStore.edit { it[KEY_READER_VOLUME_KEYS] = enabled } }
    suspend fun setReaderKeepScreen(enabled: Boolean) { dataStore.edit { it[KEY_READER_KEEP_SCREEN] = enabled } }
    suspend fun setReaderAutoMark(enabled: Boolean) { dataStore.edit { it[KEY_READER_AUTO_MARK] = enabled } }
    suspend fun setReaderPreload(amount: Int) { dataStore.edit { it[KEY_READER_PRELOAD] = amount } }
    suspend fun setDownloadAsCbz(enabled: Boolean) { dataStore.edit { it[KEY_DOWNLOAD_AS_CBZ] = enabled } }
    suspend fun setMaxConcurrentDownloads(count: Int) { dataStore.edit { it[KEY_MAX_CONCURRENT_DOWNLOADS] = count } }
    suspend fun setShowNsfw(enabled: Boolean) { dataStore.edit { it[KEY_SHOW_NSFW] = enabled } }
    suspend fun setAutoBackupEnabled(enabled: Boolean) { dataStore.edit { it[KEY_AUTO_BACKUP] = enabled } }
    suspend fun setAutoBackupInterval(hours: Int) { dataStore.edit { it[KEY_AUTO_BACKUP_INTERVAL] = hours } }
    suspend fun setIsTrueBlack(enabled: Boolean) { dataStore.edit { it[KEY_IS_TRUE_BLACK] = enabled } }
    suspend fun setGridCoverWidth(width: Int) { dataStore.edit { it[KEY_GRID_COVER_WIDTH] = width } }
    suspend fun setDefaultLibrarySort(sort: String) { dataStore.edit { it[KEY_DEFAULT_LIBRARY_SORT] = sort } }
    suspend fun setDownloadChapters(amount: Int) { dataStore.edit { it[KEY_DOWNLOAD_CHAPTERS] = amount } }
    suspend fun setLanguageString(lang: String) { dataStore.edit { it[KEY_LANGUAGE] = lang } }
}
