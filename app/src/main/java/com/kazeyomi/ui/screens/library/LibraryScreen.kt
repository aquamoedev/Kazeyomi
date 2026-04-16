package com.kazeyomi.ui.screens.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kazeyomi.domain.model.Category
import com.kazeyomi.domain.model.Manga
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onMangaClick: (Int) -> Unit,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSortMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadLibrary()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Library") },
                actions = {
                    IconButton(onClick = { viewModel.refreshLibrary() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Default.Sort, contentDescription = "Sort")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.categories.isNotEmpty()) {
                CategoryTabs(
                    categories = uiState.categories,
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = { viewModel.selectCategory(it) }
                )
            }

            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.error != null -> ErrorMessage(
                    message = uiState.error!!,
                    onRetry = { viewModel.loadLibrary() }
                )
                uiState.mangas.isEmpty() -> EmptyState(
                    icon = { Icon(Icons.Default.LibraryBooks, contentDescription = null, modifier = Modifier.size(64.dp)) },
                    title = "Your library is empty",
                    subtitle = "Add manga from Browse to see them here"
                )
                else -> MangaGrid(
                    mangas = uiState.mangas,
                    onMangaClick = onMangaClick
                )
            }
        }
    }

    DropdownMenu(
        expanded = showSortMenu,
        onDismissRequest = { showSortMenu = false }
    ) {
        DropdownMenuItem(
            text = { Text("Title") },
            onClick = {
                viewModel.setSortBy("title")
                showSortMenu = false
            },
            leadingIcon = { Icon(Icons.Default.SortByAlpha, contentDescription = null) }
        )
        DropdownMenuItem(
            text = { Text("Last Read") },
            onClick = {
                viewModel.setSortBy("lastRead")
                showSortMenu = false
            },
            leadingIcon = { Icon(Icons.Default.History, contentDescription = null) }
        )
        DropdownMenuItem(
            text = { Text("Date Added") },
            onClick = {
                viewModel.setSortBy("dateAdded")
                showSortMenu = false
            },
            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) }
        )
    }
}

@Composable
fun CategoryTabs(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
        modifier = Modifier.fillMaxWidth()
    ) {
        Tab(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            text = { Text("All") }
        )
        categories.forEach { category ->
            Tab(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                text = { Text("${category.name} (${category.size})") }
            )
        }
    }
}

@Composable
fun MangaGrid(
    mangas: List<Manga>,
    onMangaClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(mangas, key = { it.id }) { manga ->
            MangaGridItem(
                manga = manga,
                onClick = { onMangaClick(manga.id) }
            )
        }
    }
}
