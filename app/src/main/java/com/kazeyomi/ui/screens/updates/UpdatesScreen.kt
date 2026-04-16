package com.kazeyomi.ui.screens.updates

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
import com.kazeyomi.domain.model.Manga
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatesScreen(
    onMangaClick: (Int) -> Unit,
    viewModel: UpdatesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUpdates()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Updates") },
                actions = {
                    IconButton(onClick = { viewModel.loadUpdates() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(paddingValues))
            uiState.error != null -> ErrorMessage(
                message = uiState.error!!,
                onRetry = { viewModel.loadUpdates() },
                modifier = Modifier.padding(paddingValues)
            )
            uiState.updates.isEmpty() -> EmptyState(
                icon = { Icon(Icons.Default.Update, contentDescription = null, modifier = Modifier.size(64.dp)) },
                title = "No updates",
                subtitle = "Check back later for new chapters",
                modifier = Modifier.padding(paddingValues)
            )
            else -> UpdatesList(
                updates = uiState.updates,
                onMangaClick = onMangaClick,
                onMarkRead = { viewModel.markAsRead(it) },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun UpdatesList(
    updates: List<UpdateUiModel>,
    onMangaClick: (Int) -> Unit,
    onMarkRead: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(updates, key = { "${it.mangaId}-${it.chapterId}" }) { update ->
            UpdateItem(
                update = update,
                onClick = { onMangaClick(update.mangaId) },
                onMarkRead = { onMarkRead(update.chapterId) }
            )
        }
    }
}

@Composable
fun UpdateItem(
    update: UpdateUiModel,
    onClick: () -> Unit,
    onMarkRead: () -> Unit
) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        headlineContent = { Text(update.mangaTitle) },
        supportingContent = { Text(update.chapterName) },
        leadingContent = {
            Icon(
                imageVector = if (update.isUnread) Icons.Default.FiberNew else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (update.isUnread) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            if (update.isUnread) {
                TextButton(onClick = onMarkRead) {
                    Text("Mark Read")
                }
            }
        }
    )
    Divider()
}
