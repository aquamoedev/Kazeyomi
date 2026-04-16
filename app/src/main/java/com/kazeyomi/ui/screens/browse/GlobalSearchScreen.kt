package com.kazeyomi.ui.screens.browse

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
fun GlobalSearchScreen(
    onBackClick: () -> Unit,
    onMangaClick: (Int) -> Unit,
    viewModel: GlobalSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Global Search") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { viewModel.search(searchQuery) },
                placeholder = "Search manga..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.error != null -> ErrorMessage(
                    message = uiState.error!!,
                    onRetry = { viewModel.search(searchQuery) }
                )
                uiState.results.isEmpty() && searchQuery.isNotEmpty() -> EmptyState(
                    icon = { Icon(Icons.Default.SearchOff, contentDescription = null, modifier = Modifier.size(64.dp)) },
                    title = "No results found",
                    subtitle = "Try a different search term"
                )
                uiState.results.isEmpty() -> EmptyState(
                    icon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(64.dp)) },
                    title = "Search for manga",
                    subtitle = "Enter a title to search across all sources"
                )
                else -> SearchResults(
                    results = uiState.results,
                    onMangaClick = onMangaClick
                )
            }
        }
    }
}

@Composable
fun SearchResults(
    results: List<com.kazeyomi.domain.model.Manga>,
    onMangaClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(results, key = { "${it.sourceId}-${it.id}" }) { manga ->
            MangaGridItem(
                manga = manga,
                onClick = { onMangaClick(manga.id) }
            )
        }
    }
}
