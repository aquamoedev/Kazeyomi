package com.kazeyomi.ui.screens.downloads
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
import com.kazeyomi.domain.model.DownloadState
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsScreen(onBackClick: () -> Unit, onMangaClick: (Int) -> Unit, viewModel: DownloadsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.load() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Downloads") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }, actions = { IconButton(onClick = { viewModel.clear() }) { Icon(Icons.Default.Delete, "Clear") } }) }
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(Modifier.padding(padding))
            state.error != null -> ErrorMessage(state.error!!, { viewModel.load() }, Modifier.padding(padding))
            state.downloads.isEmpty() -> EmptyState("No downloads", "Downloads will appear here", modifier = Modifier.padding(padding))
            else -> LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                items(state.downloads, key = { it.chapterId }) { dl ->
                    ListItem(
                        headlineContent = { Text(dl.mangaTitle) },
                        supportingContent = { if (dl.state == DownloadState.DOWNLOADING) LinearProgressIndicator(progress = { dl.progress / 100f }, modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) else Text("${dl.chapterName}  •  ${dl.state.name}") },
                        modifier = Modifier.clickable { onMangaClick(dl.mangaId) },
                        trailingContent = { if (dl.state != DownloadState.DOWNLOADED) IconButton(onClick = { viewModel.cancel(dl.chapterId) }) { Icon(Icons.Default.Close, "Cancel") } }
                    )
                    Divider()
                }
            }
        }
    }
}
