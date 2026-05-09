package com.kazeyomi.ui.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreMenuScreen(
    onHistoryClick: () -> Unit, onDownloadsClick: () -> Unit, onMigrationClick: () -> Unit,
    onAboutClick: () -> Unit, onSettingsClick: () -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("More") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { ListItem(headlineContent = { Text("History") }, supportingContent = { Text("Reading history") }, leadingContent = { Icon(Icons.Default.History, null) }, modifier = Modifier.clickable(onClick = onHistoryClick)); Divider() }
            item { ListItem(headlineContent = { Text("Downloads") }, supportingContent = { Text("Manage downloads") }, leadingContent = { Icon(Icons.Default.Download, null) }, modifier = Modifier.clickable(onClick = onDownloadsClick)); Divider() }
            item { ListItem(headlineContent = { Text("Migration") }, supportingContent = { Text("Migrate manga between sources") }, leadingContent = { Icon(Icons.Default.SwapHoriz, null) }, modifier = Modifier.clickable(onClick = onMigrationClick)); Divider() }
            item { ListItem(headlineContent = { Text("About") }, supportingContent = { Text("App and server info") }, leadingContent = { Icon(Icons.Default.Info, null) }, modifier = Modifier.clickable(onClick = onAboutClick)); Divider() }
            item { ListItem(headlineContent = { Text("Settings") }, supportingContent = { Text("Configure the app") }, leadingContent = { Icon(Icons.Default.Settings, null) }, modifier = Modifier.clickable(onClick = onSettingsClick)); Divider() }
        }
    }
}
