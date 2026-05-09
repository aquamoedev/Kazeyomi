package com.kazeyomi.ui.screens.browse
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kazeyomi.domain.model.Source
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcesScreen(onBackClick: () -> Unit, onSourceClick: (String, String) -> Unit, viewModel: SourcesViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var showNsfw by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { viewModel.loadSources() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Sources") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }, actions = { IconButton(onClick = { showNsfw = !showNsfw }) { Icon(if (showNsfw) Icons.Default.VisibilityOff else Icons.Default.Visibility, "Toggle NSFW") } }) }
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(Modifier.padding(padding))
            state.error != null -> ErrorMessage(state.error!!, { viewModel.loadSources() }, Modifier.padding(padding))
            else -> {
                val filtered = state.sources.filter { !it.isNsfw || showNsfw }
                if (filtered.isEmpty()) EmptyState("No sources", "Install extensions to get sources", modifier = Modifier.padding(padding))
                else LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                    items(filtered, key = { it.id }) { source ->
                        ListItem(
                            headlineContent = { Text(source.displayName.ifEmpty { source.name }) },
                            supportingContent = { Text("${source.language}  •  v${source.version}") },
                            modifier = Modifier.clickable { onSourceClick(source.id, source.name) },
                            trailingContent = { if (source.isNsfw) Icon(Icons.Default.Warning, "NSFW", tint = MaterialTheme.colorScheme.error) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}
