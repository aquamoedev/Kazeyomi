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
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    onSourceClick: (String) -> Unit,
    onExtensionsClick: () -> Unit,
    onSourcesClick: () -> Unit,
    onGlobalSearchClick: () -> Unit,
    viewModel: BrowseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSources()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Browse") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                ListItem(
                    headlineContent = { Text("Global Search") },
                    supportingContent = { Text("Search across all sources") },
                    leadingContent = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable(onClick = onGlobalSearchClick)
                )
                HorizontalDivider()
            }

            item {
                ListItem(
                    headlineContent = { Text("Extensions") },
                    supportingContent = { Text("Manage manga sources") },
                    leadingContent = { Icon(Icons.Default.Extension, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable(onClick = onExtensionsClick)
                )
                HorizontalDivider()
            }

            item {
                ListItem(
                    headlineContent = { Text("Sources") },
                    supportingContent = { Text("Browse installed sources") },
                    leadingContent = { Icon(Icons.Default.Source, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable(onClick = onSourcesClick)
                )
                HorizontalDivider()
            }

            item {
                SectionHeader(title = "Popular Sources")
            }

            when {
                uiState.isLoading -> item { LoadingIndicator(modifier = Modifier.height(200.dp)) }
                uiState.error != null -> item {
                    ErrorMessage(
                        message = uiState.error!!,
                        onRetry = { viewModel.loadSources() },
                        modifier = Modifier.height(200.dp)
                    )
                }
                else -> items(uiState.sources.take(10)) { source ->
                    ListItem(
                        headlineContent = { Text(source.name) },
                        supportingContent = { Text(source.language) },
                        leadingContent = {
                            Icon(
                                Icons.Default.Source,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        modifier = Modifier.clickable { onSourceClick(source.id) }
                    )
                }
            }
        }
    }
}
