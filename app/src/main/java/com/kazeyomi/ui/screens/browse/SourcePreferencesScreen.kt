package com.kazeyomi.ui.screens.browse
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcePreferencesScreen(sourceId: String, sourceName: String, onBackClick: () -> Unit, viewModel: SourcePreferencesViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(sourceId) { viewModel.load(sourceId) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("$sourceName Settings") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }, actions = { TextButton(onClick = { viewModel.save() }) { Text("Save") } }) }
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(Modifier.padding(padding))
            state.error != null -> ErrorMessage(state.error!!, { viewModel.load(sourceId) }, Modifier.padding(padding))
            state.preferences.isEmpty() -> EmptyState("No preferences", "This source has no settings", modifier = Modifier.padding(padding))
            else -> LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.preferences.forEach { (key, value) ->
                    item {
                        when {
                            value == "true" || value == "false" -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(key); Switch(checked = value == "true", onCheckedChange = { viewModel.update(key, it.toString()) }) }
                            else -> OutlinedTextField(value = value, onValueChange = { viewModel.update(key, it) }, label = { Text(key) }, modifier = Modifier.fillMaxWidth())
                        }
                        Divider()
                    }
                }
            }
        }
    }
}
