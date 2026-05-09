package com.kazeyomi.ui.screens.browse
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun SourceBrowseScreen(sourceId: String, sourceName: String, onBackClick: () -> Unit, onMangaClick: (Int) -> Unit, viewModel: SourceBrowseViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val tabs = listOf("Popular", "Latest", "Search")
    val pagerState = rememberPagerState { 3 }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(sourceId) { viewModel.init(sourceId) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(sourceName.ifEmpty { "Source" }) }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { i, t -> Tab(selected = pagerState.currentPage == i, onClick = {}, text = { Text(t) }) }
            }
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                when (page) {
                    0 -> SourceMangaList(state.popular, state.popularLoading, state.popularError, onMangaClick, { viewModel.loadMorePopular() }, { viewModel.loadPopular() })
                    1 -> SourceMangaList(state.latest, state.latestLoading, state.latestError, onMangaClick, { viewModel.loadMoreLatest() }, { viewModel.loadLatest() })
                    2 -> Column(Modifier.fillMaxSize()) {
                        OutlinedTextField(value = searchQuery, onValueChange = { searchQuery = it }, modifier = Modifier.fillMaxWidth().padding(16.dp), placeholder = { Text("Search this source...") }, leadingIcon = { Icon(Icons.Default.Search, null) }, trailingIcon = { if (searchQuery.isNotEmpty()) IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Clear, null) } }, singleLine = true)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { if (searchQuery.isNotBlank()) viewModel.search(searchQuery) }, modifier = Modifier.padding(horizontal = 16.dp)) { Text("Search") }
                        when {
                            state.searchLoading -> LoadingIndicator()
                            state.searchError != null -> ErrorMessage(state.searchError!!, { viewModel.search(searchQuery) })
                            state.searchResults.isNotEmpty() -> MangaGrid(state.searchResults, onMangaClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SourceMangaList(mangas: List<com.kazeyomi.domain.model.Manga>, loading: Boolean, error: String?, onMangaClick: (Int) -> Unit, onLoadMore: () -> Unit, onRetry: () -> Unit) {
    when {
        loading && mangas.isEmpty() -> LoadingIndicator()
        error != null && mangas.isEmpty() -> ErrorMessage(error, onRetry)
        mangas.isEmpty() -> EmptyState("No manga found", "Try a different source")
        else -> MangaGrid(mangas, onMangaClick)
    }
}
