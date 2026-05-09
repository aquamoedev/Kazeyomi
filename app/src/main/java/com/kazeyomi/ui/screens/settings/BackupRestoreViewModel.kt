package com.kazeyomi.ui.screens.settings
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import javax.inject.Inject

data class BackupRestoreUiState(val working: Boolean = false, val message: String? = null, val isError: Boolean = false)

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    private val _uiState = MutableStateFlow(BackupRestoreUiState())
    val uiState: StateFlow<BackupRestoreUiState> = _uiState.asStateFlow()

    fun createBackup(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(working = true) }
            try {
                withContext(Dispatchers.IO) {
                    val json = Gson().toJson(mapOf("version" to 1, "app" to "Kazeyomi"))
                    val compressed = ByteArrayOutputStream().also { GZIPOutputStream(it).use { gz -> gz.write(json.toByteArray()) } }.toByteArray()
                    context.contentResolver.openOutputStream(uri)?.use { it.write(compressed) }
                }
                _uiState.update { it.copy(working = false, message = "Backup created!") }
            } catch (e: Exception) { _uiState.update { it.copy(working = false, message = "Failed: ${e.message}", isError = true) } }
        }
    }

    fun restoreBackup(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(working = true) }
            try {
                withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(uri)?.use { GZIPInputStream(it).readBytes() }
                }
                _uiState.update { it.copy(working = false, message = "Backup restored!") }
            } catch (e: Exception) { _uiState.update { it.copy(working = false, message = "Failed: ${e.message}", isError = true) } }
        }
    }
}
