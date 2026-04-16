package com.kazeyomi.ui.screens.history

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
import com.kazeyomi.domain.model.History
import com.kazeyomi.ui.components.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    onMangaClick: (Int) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    if (showClearDialog) {
        ConfirmDialog(
            title = "Clear History",
            message = "Are you sure you want to clear all reading history?",
            confirmText = "Clear",
            onConfirm = {
                viewModel.clearHistory()
                showClearDialog = false
            },
            onDismiss = { showClearDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.history.isNotEmpty()) {
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
                onRetry = { viewModel.loadHistory() },
                modifier = Modifier.padding(paddingValues)
            )
            uiState.history.isEmpty() -> EmptyState(
                icon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(64.dp)) },
                title = "No reading history",
                subtitle = "Start reading to see your history here",
                modifier = Modifier.padding(paddingValues)
            )
            else -> HistoryList(
                history = uiState.history,
                onMangaClick = onMangaClick,
                onDeleteItem = { viewModel.deleteHistoryItem(it) },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun HistoryList(
    history: List<History>,
    onMangaClick: (Int) -> Unit,
    onDeleteItem: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val groupedHistory = history.groupBy { historyItem ->
        val date = Date(historyItem.lastReadAt)
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        groupedHistory.forEach { (date, items) ->
            item {
                SectionHeader(title = formatDate(date))
            }
            items(items, key = { it.id }) { historyItem ->
                HistoryItem(
                    history = historyItem,
                    onClick = { onMangaClick(historyItem.mangaId) },
                    onDelete = { onDeleteItem(historyItem.id) }
                )
            }
        }
    }
}

@Composable
fun HistoryItem(
    history: History,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    ListItem(
        headlineContent = { Text(history.mangaTitle) },
        supportingContent = { Text(history.chapterTitle) },
        leadingContent = {
            Icon(Icons.Default.History, contentDescription = null)
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
    Divider()
}

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}
