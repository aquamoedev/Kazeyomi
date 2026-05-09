package com.kazeyomi.ui.screens.library

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
fun LibraryScreen(
    onMangaClick: (Int) -> Unit,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showSortMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.loadLibrary() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Library") },
                actions = {
                    IconButton(onClick = { viewModel.refreshLibrary() }) { Icon(Icons.Default.Refresh, "Refresh") }
                    IconButton(onClick = { showSortMenu = true }) { Icon(Icons.Default.Sort, "Sort") }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            if (state.categories.isNotEmpty()) {
                ScrollableTabRow(
                    selectedTabIndex = state.categories.indexOf(state.selectedCategory).coerceAtLeast(0),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(selected = state.selectedCategory == null, onClick = { viewModel.selectCategory(null) }, text = { Text("All") })
                    state.categories.forEach { cat ->
                        Tab(selected = state.selectedCategory == cat, onClick = { viewModel.selectCategory(cat) }, text = { Text("${cat.name} (${cat.size})") })
                    }
                }
            }
            when {
                state.isLoading && state.mangas.isEmpty() -> LoadingIndicator()
                state.error != null && state.mangas.isEmpty() -> ErrorMessage(state.error!!, { viewModel.loadLibrary() })
                state.mangas.isEmpty() -> EmptyState("Library is empty", "Explore sources to add manga")
                else -> MangaGrid(state.mangas, onMangaClick)
            }
        }
    }

    DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
        listOf("title" to "Title", "lastRead" to "Last Read", "dateAdded" to "Date Added").forEach { (key, label) ->
            DropdownMenuItem(text = { Text(label) }, onClick = { viewModel.setSortBy(key); showSortMenu = false }, leadingIcon = { Icon(Icons.Default.SortByAlpha, null) })
        }
    }
}
