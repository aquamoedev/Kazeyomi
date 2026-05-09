package com.kazeyomi.ui.screens.settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit, onServerSetupClick: () -> Unit, onReaderSettingsClick: () -> Unit,
    onAppearanceClick: () -> Unit, onBrowseSettingsClick: () -> Unit, onDownloadsSettingsClick: () -> Unit,
    onLibrarySettingsClick: () -> Unit, onBackupRestoreClick: () -> Unit, onCategoryManagementClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { SectionHeader("Server") }
            item { ListItem(headlineContent = { Text("Server Connection") }, supportingContent = { Text(state.serverUrl.ifEmpty { "Not configured" }) }, leadingContent = { Icon(Icons.Default.Cloud, null) }, modifier = Modifier.clickable(onClick = onServerSetupClick)); Divider() }
            item { SectionHeader("Appearance") }
            item { ListItem(headlineContent = { Text("Theme & Display") }, supportingContent = { Text("Theme, colors, grid size") }, leadingContent = { Icon(Icons.Default.Palette, null) }, modifier = Modifier.clickable(onClick = onAppearanceClick)); Divider() }
            item { SectionHeader("Library") }
            item { ListItem(headlineContent = { Text("Categories") }, supportingContent = { Text("Manage library categories") }, leadingContent = { Icon(Icons.Default.Folder, null) }, modifier = Modifier.clickable(onClick = onCategoryManagementClick)); Divider() }
            item { ListItem(headlineContent = { Text("Library Settings") }, supportingContent = { Text("Display, sorting") }, leadingContent = { Icon(Icons.Default.AutoStories, null) }, modifier = Modifier.clickable(onClick = onLibrarySettingsClick)); Divider() }
            item { SectionHeader("Reader") }
            item { ListItem(headlineContent = { Text("Reader Settings") }, supportingContent = { Text("Mode, direction, controls") }, leadingContent = { Icon(Icons.Default.MenuBook, null) }, modifier = Modifier.clickable(onClick = onReaderSettingsClick)); Divider() }
            item { SectionHeader("Browse") }
            item { ListItem(headlineContent = { Text("Browse Settings") }, supportingContent = { Text("NSFW, repositories") }, leadingContent = { Icon(Icons.Default.Explore, null) }, modifier = Modifier.clickable(onClick = onBrowseSettingsClick)); Divider() }
            item { SectionHeader("Downloads") }
            item { ListItem(headlineContent = { Text("Download Settings") }, supportingContent = { Text("Concurrency, format") }, leadingContent = { Icon(Icons.Default.Download, null) }, modifier = Modifier.clickable(onClick = onDownloadsSettingsClick)); Divider() }
            item { SectionHeader("Data") }
            item { ListItem(headlineContent = { Text("Backup & Restore") }, supportingContent = { Text("Save and restore library") }, leadingContent = { Icon(Icons.Default.Backup, null) }, modifier = Modifier.clickable(onClick = onBackupRestoreClick)); Divider() }
            item { ListItem(headlineContent = { Text("Kazeyomi v1.0.0") }, supportingContent = { Text("Native Kotlin manga reader") }, leadingContent = { Icon(Icons.Default.AutoStories, null, tint = MaterialTheme.colorScheme.primary) }) }
        }
    }
}
