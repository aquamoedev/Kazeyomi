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
import androidx.hilt.navigation.compose.hiltViewModel
import com.kazeyomi.ui.components.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onBackClick: () -> Unit, onMangaClick: (Int) -> Unit, viewModel: HistoryViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.load() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("History") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }, actions = { IconButton(onClick = { viewModel.clear() }) { Icon(Icons.Default.Delete, "Clear") } }) }
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(Modifier.padding(padding))
            state.error != null -> ErrorMessage(state.error!!, { viewModel.load() }, Modifier.padding(padding))
            state.items.isEmpty() -> EmptyState("No history", "Read chapters will appear here", modifier = Modifier.padding(padding))
            else -> LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                items(state.items, key = { it.id }) { h ->
                    ListItem(
                        headlineContent = { Text(h.mangaTitle) },
                        supportingContent = { Text("${h.chapterTitle}  •  ${SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(h.lastReadAt))}") },
                        modifier = Modifier.clickable { onMangaClick(h.mangaId) },
                        trailingContent = { IconButton(onClick = { viewModel.delete(h.id) }) { Icon(Icons.Default.Close, "Remove") } }
                    )
                    Divider()
                }
            }
        }
    }
}
