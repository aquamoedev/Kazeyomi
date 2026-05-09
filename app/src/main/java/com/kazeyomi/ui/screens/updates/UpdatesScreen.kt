package com.kazeyomi.ui.screens.updates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kazeyomi.ui.components.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatesScreen(
    onMangaClick: (Int) -> Unit,
    viewModel: UpdatesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadUpdates() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Updates") }, actions = { IconButton(onClick = { viewModel.loadUpdates() }) { Icon(Icons.Default.Refresh, "Refresh") } }) }
    ) { padding ->
        when {
            state.isLoading && state.updates.isEmpty() -> LoadingIndicator(Modifier.padding(padding))
            state.error != null && state.updates.isEmpty() -> ErrorMessage(state.error!!, { viewModel.loadUpdates() }, Modifier.padding(padding))
            state.updates.isEmpty() -> EmptyState("No updates", "New chapters will appear here", modifier = Modifier.padding(padding))
            else -> LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                items(state.updates) { update ->
                    ListItem(
                        headlineContent = { Text(update.mangaTitle, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        supportingContent = { Text("${update.chapterName}  •  ${formatTime(update.timestamp)}") },
                        modifier = Modifier.clickable { onMangaClick(update.mangaId) },
                        leadingContent = { Icon(Icons.Default.Circle, null, Modifier.size(12.dp).padding(top = 4.dp), tint = MaterialTheme.colorScheme.primary) }
                    )
                    Divider()
                }
            }
        }
    }
}

fun formatTime(millis: Long): String {
    if (millis == 0L) return ""
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(millis))
}
