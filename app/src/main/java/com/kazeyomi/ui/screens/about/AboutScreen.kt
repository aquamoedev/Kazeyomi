package com.kazeyomi.ui.screens.about
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBackClick: () -> Unit, viewModel: AboutViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.load() }

    Scaffold(topBar = { TopAppBar(title = { Text("About") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ListItem(headlineContent = { Text("Kazeyomi") }, supportingContent = { Text("Native Kotlin manga reader") }, leadingContent = { Icon(Icons.Default.AutoStories, null, tint = MaterialTheme.colorScheme.primary) })
            ListItem(headlineContent = { Text("Version") }, supportingContent = { Text("1.0.0") }, leadingContent = { Icon(Icons.Default.Info, null) })
            if (state.serverInfo != null) {
                val s = state.serverInfo!!
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) { Text("Server", style = MaterialTheme.typography.titleSmall); Spacer(Modifier.height(4.dp)); Text("Version: ${s.versionName}"); Text("API: v${s.apiVersion}");  }
                }
            }
            state.error?.let { Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) { Text(it, Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onErrorContainer) } }
            Text("Kazeyomi is a free and open source manga reader based on Kotlin native Android. It connects to a Suwayomi Server instance to read manga.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
