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
fun SourcesScreen(
    onBackClick: () -> Unit,
    onSourceClick: (String) -> Unit,
    viewModel: SourcesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showNsfw by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadSources()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sources") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showNsfw = !showNsfw }) {
                        Icon(
                            if (showNsfw) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle NSFW"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(paddingValues))
            uiState.error != null -> ErrorMessage(
                message = uiState.error!!,
                onRetry = { viewModel.loadSources() },
                modifier = Modifier.padding(paddingValues)
            )
            else -> SourcesList(
                sources = uiState.sources.filter { !it.isNsfw || showNsfw },
                onSourceClick = onSourceClick,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun SourcesList(
    sources: List<Source>,
    onSourceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (sources.isEmpty()) {
        EmptyState(
            icon = { Icon(Icons.Default.Source, contentDescription = null, modifier = Modifier.size(64.dp)) },
            title = "No sources installed",
            subtitle = "Install extensions to get sources",
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(sources, key = { it.id }) { source ->
                SourceItem(
                    source = source,
                    onClick = { onSourceClick(source.id) }
                )
            }
        }
    }
}

@Composable
fun SourceItem(
    source: Source,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(source.name) },
        supportingContent = { Text("${source.language} • v${source.version}") },
        leadingContent = {
            Icon(
                Icons.Default.Source,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        },
        trailingContent = {
            if (source.isNsfw) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "NSFW",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
    Divider()
}
