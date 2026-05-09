package com.kazeyomi.ui.screens.browse
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalSearchScreen(onBackClick: () -> Unit, onMangaClick: (Int) -> Unit, viewModel: GlobalSearchViewModel = hiltViewModel()) {
    var query by remember { mutableStateOf("") }
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = { viewModel.clear(); onBackClick() }) { Icon(Icons.Default.ArrowBack, "Back") } },
                title = {
                    OutlinedTextField(value = query, onValueChange = { query = it; viewModel.search(it) },
                        placeholder = { Text("Search all sources...") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0f), unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0f)))
                },
                actions = { if (query.isNotEmpty()) { IconButton(onClick = { query = ""; viewModel.clear() }) { Icon(Icons.Default.Clear, "Clear") } } }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(Modifier.padding(padding))
            state.error != null -> ErrorMessage(state.error!!, { viewModel.search(query) }, Modifier.padding(padding))
            state.results.isNotEmpty() -> MangaGrid(state.results, onMangaClick)
        }
    }
}
