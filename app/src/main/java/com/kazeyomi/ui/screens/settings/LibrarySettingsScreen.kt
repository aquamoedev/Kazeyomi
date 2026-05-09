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
fun LibrarySettingsScreen(onBackClick: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val s by viewModel.library.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Library Settings") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text("Hide Empty Categories"); Switch(checked = s.hideEmptyCategories, onCheckedChange = { viewModel.setHideEmptyCategories(it) }) }
            Divider(Modifier.padding(vertical = 8.dp))
            Text("Default Sort: ${s.defaultSort}", style = MaterialTheme.typography.titleMedium)
            listOf("title", "lastRead", "dateAdded", "unreadCount").forEach { sort ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(sort); RadioButton(selected = s.defaultSort == sort, onClick = { viewModel.setDefaultLibrarySort(sort) }) }
            }
        }
    }
}
