package com.kazeyomi.ui.screens.downloads

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kazeyomi.domain.model.Download
import com.kazeyomi.domain.model.DownloadState
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsScreen(
    onBackClick: () -> Unit,
    onMangaClick: (Int) -> Unit,
    viewModel: DownloadsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadDownloads()
    }

    if (showClearDialog) {
        ConfirmDialog(
            title = "Clear All Downloads",
            message = "Are you sure you want to cancel all downloads?",
            confirmText = "Clear",
            onConfirm = {
                viewModel.clearAllDownloads()
                showClearDialog = false
            },
            onDismiss = { showClearDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Downloads") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.downloads.isNotEmpty()) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Clear All")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(paddingValues))
            uiState.error != null -> ErrorMessage(
                message = uiState.error!!,
                onRetry = { viewModel.loadDownloads() },
                modifier = Modifier.padding(paddingValues)
            )
            uiState.downloads.isEmpty() -> EmptyState(
                icon = { Icon(Icons.Default.DownloadDone, contentDescription = null, modifier = Modifier.size(64.dp)) },
                title = "No downloads",
                subtitle = "Downloaded chapters will appear here",
                modifier = Modifier.padding(paddingValues)
            )
            else -> DownloadsList(
                downloads = uiState.downloads,
                onMangaClick = onMangaClick,
                onCancelDownload = { viewModel.cancelDownload(it) },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun DownloadsList(
    downloads: List<Download>,
    onMangaClick: (Int) -> Unit,
    onCancelDownload: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(downloads, key = { it.chapterId }) { download ->
            DownloadItem(
                download = download,
                onClick = { onMangaClick(download.mangaId) },
                onCancel = { onCancelDownload(download.chapterId) }
            )
        }
    }
}

@Composable
fun DownloadItem(
    download: Download,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    ListItem(
        headlineContent = { Text(download.mangaTitle) },
        supportingContent = {
            Column {
                Text(download.chapterName)
                when (download.state) {
                    DownloadState.DOWNLOADING -> {
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = download.progress / 100f,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            "${download.downloadedPages}/${download.totalPages} pages",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    DownloadState.QUEUED -> {
                        Text("Queued", style = MaterialTheme.typography.bodySmall)
                    }
                    DownloadState.ERROR -> {
                        Text(
                            "Error - Tap to retry",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else -> {}
                }
            }
        },
        leadingContent = {
            when (download.state) {
                DownloadState.DOWNLOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
                DownloadState.DOWNLOADED -> {
                    Icon(
                        Icons.Default.DownloadDone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                DownloadState.ERROR -> {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    Icon(Icons.Default.Download, contentDescription = null)
                }
            }
        },
        trailingContent = {
            if (download.state == DownloadState.DOWNLOADING || download.state == DownloadState.QUEUED) {
                IconButton(onClick = onCancel) {
                    Icon(Icons.Default.Close, contentDescription = "Cancel")
                }
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
    Divider()
}
