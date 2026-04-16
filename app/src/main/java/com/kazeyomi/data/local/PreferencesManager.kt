package com.kazeyomi.data.local

import android.content.Context
import androidx.datastore.core.DataStore
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

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val SERVER_URL = stringPreferencesKey("server_url")
        val SERVER_USERNAME = stringPreferencesKey("server_username")
        val SERVER_PASSWORD = stringPreferencesKey("server_password")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val LANGUAGE = stringPreferencesKey("language")
        val LIBRARY_DISPLAY_MODE = stringPreferencesKey("library_display_mode")
        val LIBRARY_SORT_BY = stringPreferencesKey("library_sort_by")
        val LIBRARY_SORT_DESC = booleanPreferencesKey("library_sort_desc")
        val READER_MODE = stringPreferencesKey("reader_mode")
        val READER_DIRECTION = stringPreferencesKey("reader_direction")
        val READER_TAP_ZONES = stringPreferencesKey("reader_tap_zones")
        val READER_SHOW_PAGENUM = booleanPreferencesKey("reader_show_pagenum")
        val READER_VOLUME_KEYS = booleanPreferencesKey("reader_volume_keys")
        val READER_KEEP_SCREEN = booleanPreferencesKey("reader_keep_screen")
        val READER_AUTO_MARK = booleanPreferencesKey("reader_auto_mark")
        val READER_PRELOAD = intPreferencesKey("reader_preload")
        val DOWNLOAD_AS_CBZ = booleanPreferencesKey("download_as_cbz")
        val DOWNLOAD_CHAPTERS = intPreferencesKey("download_chapters")
        val AUTO_UPDATE_LIBRARY = booleanPreferencesKey("auto_update_library")
        val UPDATE_INTERVAL = intPreferencesKey("update_interval")
    }

    val serverUrl: Flow<String> = dataStore.data.map { it[SERVER_URL] ?: "" }
    val serverUsername: Flow<String?> = dataStore.data.map { it[SERVER_USERNAME] }
    val serverPassword: Flow<String?> = dataStore.data.map { it[SERVER_PASSWORD] }
    val themeMode: Flow<String> = dataStore.data.map { it[THEME_MODE] ?: "system" }
    val dynamicColor: Flow<Boolean> = dataStore.data.map { it[DYNAMIC_COLOR] ?: true }
    val language: Flow<String> = dataStore.data.map { it[LANGUAGE] ?: "en" }
    val libraryDisplayMode: Flow<String> = dataStore.data.map { it[LIBRARY_DISPLAY_MODE] ?: "grid" }
    val librarySortBy: Flow<String> = dataStore.data.map { it[LIBRARY_SORT_BY] ?: "title" }
    val librarySortDesc: Flow<Boolean> = dataStore.data.map { it[LIBRARY_SORT_DESC] ?: true }
    val readerMode: Flow<String> = dataStore.data.map { it[READER_MODE] ?: "VERTICAL" }
    val readerDirection: Flow<String> = dataStore.data.map { it[READER_DIRECTION] ?: "LEFT_TO_RIGHT" }
    val readerTapZones: Flow<String> = dataStore.data.map { it[READER_TAP_ZONES] ?: "STANDARD" }
    val readerShowPageNum: Flow<Boolean> = dataStore.data.map { it[READER_SHOW_PAGENUM] ?: true }
    val readerVolumeKeys: Flow<Boolean> = dataStore.data.map { it[READER_VOLUME_KEYS] ?: true }
    val readerKeepScreen: Flow<Boolean> = dataStore.data.map { it[READER_KEEP_SCREEN] ?: true }
    val readerAutoMark: Flow<Boolean> = dataStore.data.map { it[READER_AUTO_MARK] ?: true }
    val readerPreload: Flow<Int> = dataStore.data.map { it[READER_PRELOAD] ?: 5 }
    val downloadAsCbz: Flow<Boolean> = dataStore.data.map { it[DOWNLOAD_AS_CBZ] ?: true }
    val downloadChapters: Flow<Int> = dataStore.data.map { it[DOWNLOAD_CHAPTERS] ?: 3 }
    val autoUpdateLibrary: Flow<Boolean> = dataStore.data.map { it[AUTO_UPDATE_LIBRARY] ?: false }
    val updateInterval: Flow<Int> = dataStore.data.map { it[UPDATE_INTERVAL] ?: 60 }

    suspend fun setServerUrl(url: String) { dataStore.edit { it[SERVER_URL] = url } }
    suspend fun setServerCredentials(username: String?, password: String?) {
        dataStore.edit {
            it[SERVER_USERNAME] = username ?: ""
            it[SERVER_PASSWORD] = password ?: ""
        }
    }
    suspend fun setThemeMode(mode: String) { dataStore.edit { it[THEME_MODE] = mode } }
    suspend fun setDynamicColor(enabled: Boolean) { dataStore.edit { it[DYNAMIC_COLOR] = enabled } }
    suspend fun setLanguage(lang: String) { dataStore.edit { it[LANGUAGE] = lang } }
    suspend fun setLibraryDisplayMode(mode: String) { dataStore.edit { it[LIBRARY_DISPLAY_MODE] = mode } }
    suspend fun setLibrarySortBy(sortBy: String) { dataStore.edit { it[LIBRARY_SORT_BY] = sortBy } }
    suspend fun setLibrarySortDesc(desc: Boolean) { dataStore.edit { it[LIBRARY_SORT_DESC] = desc } }
    suspend fun setReaderMode(mode: String) { dataStore.edit { it[READER_MODE] = mode } }
    suspend fun setReaderDirection(direction: String) { dataStore.edit { it[READER_DIRECTION] = direction } }
    suspend fun setReaderTapZones(zones: String) { dataStore.edit { it[READER_TAP_ZONES] = zones } }
    suspend fun setReaderShowPageNum(show: Boolean) { dataStore.edit { it[READER_SHOW_PAGENUM] = show } }
    suspend fun setReaderVolumeKeys(enabled: Boolean) { dataStore.edit { it[READER_VOLUME_KEYS] = enabled } }
    suspend fun setReaderKeepScreen(enabled: Boolean) { dataStore.edit { it[READER_KEEP_SCREEN] = enabled } }
    suspend fun setReaderAutoMark(enabled: Boolean) { dataStore.edit { it[READER_AUTO_MARK] = enabled } }
    suspend fun setReaderPreload(amount: Int) { dataStore.edit { it[READER_PRELOAD] = amount } }
    suspend fun setDownloadAsCbz(enabled: Boolean) { dataStore.edit { it[DOWNLOAD_AS_CBZ] = enabled } }
    suspend fun setDownloadChapters(amount: Int) { dataStore.edit { it[DOWNLOAD_CHAPTERS] = amount } }
    suspend fun setAutoUpdateLibrary(enabled: Boolean) { dataStore.edit { it[AUTO_UPDATE_LIBRARY] = enabled } }
    suspend fun setUpdateInterval(minutes: Int) { dataStore.edit { it[UPDATE_INTERVAL] = minutes } }

    fun getReaderSettings(): Flow<ReaderSettings> = dataStore.data.map {
        ReaderSettings(
            mode = ReadingMode.valueOf(it[READER_MODE] ?: "VERTICAL"),
            direction = ReadingDirection.valueOf(it[READER_DIRECTION] ?: "LEFT_TO_RIGHT"),
            showPageNumber = it[READER_SHOW_PAGENUM] ?: true,
            tapZones = TapZones.valueOf(it[READER_TAP_ZONES] ?: "STANDARD"),
            volumeKeys = it[READER_VOLUME_KEYS] ?: true,
            keepScreenOn = it[READER_KEEP_SCREEN] ?: true,
            autoMarkAsRead = it[READER_AUTO_MARK] ?: true,
            preloadAmount = it[READER_PRELOAD] ?: 5
        )
    }
}
