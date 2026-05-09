package com.kazeyomi.ui.screens.browse
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    onGlobalSearchClick: () -> Unit,
    onExtensionsClick: () -> Unit,
    onSourcesClick: () -> Unit,
    onSourceClick: (String, String) -> Unit,
    viewModel: BrowseViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadSources() }

    Scaffold(topBar = { TopAppBar(title = { Text("Browse") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { ListItem(headlineContent = { Text("Global Search") }, supportingContent = { Text("Search across all sources") }, leadingContent = { Icon(Icons.Default.Search, null) }, modifier = Modifier.clickable(onClick = onGlobalSearchClick)); Divider() }
            item { ListItem(headlineContent = { Text("Extensions") }, supportingContent = { Text("Manage manga extensions") }, leadingContent = { Icon(Icons.Default.Extension, null) }, modifier = Modifier.clickable(onClick = onExtensionsClick)); Divider() }
            item { ListItem(headlineContent = { Text("Sources") }, supportingContent = { Text("Browse installed sources") }, leadingContent = { Icon(Icons.Default.Source, null) }, modifier = Modifier.clickable(onClick = onSourcesClick)); Divider() }
            item { SectionHeader("Popular Sources") }
            when {
                state.isLoading -> item { LoadingIndicator(Modifier.height(200.dp)) }
                state.error != null -> item { ErrorMessage(state.error!!, { viewModel.loadSources() }, Modifier.height(200.dp)) }
                else -> state.sources.take(10).forEach { source ->
                    item { ListItem(headlineContent = { Text(source.displayName.ifEmpty { source.name }) }, supportingContent = { Text(source.language) }, modifier = Modifier.clickable { onSourceClick(source.id, source.name) }); Divider() }
                }
            }
        }
    }
}
