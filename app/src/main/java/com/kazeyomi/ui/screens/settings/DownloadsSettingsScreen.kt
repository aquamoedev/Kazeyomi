package com.kazeyomi.ui.screens.settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsSettingsScreen(onBackClick: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val s by viewModel.downloads.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Download Settings") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Max Concurrent: ${s.maxConcurrent}", style = MaterialTheme.typography.titleMedium)
            Slider(value = s.maxConcurrent.toFloat(), onValueChange = { viewModel.setMaxConcurrentDownloads(it.toInt()) }, valueRange = 1f..10f, steps = 8)
            Divider()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text("Delete after reading"); Switch(checked = s.deleteAfterRead, onCheckedChange = { viewModel.setDeleteAfterRead(it) }) }
        }
    }
}
