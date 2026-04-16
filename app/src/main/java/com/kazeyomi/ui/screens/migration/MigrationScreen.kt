package com.kazeyomi.ui.screens.migration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MigrationScreen(
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedManga by remember { mutableStateOf<com.kazeyomi.domain.model.Manga?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Migration") },
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
            Text(
                text = "Migrate your reading progress to a different source",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { },
                placeholder = "Search manga to migrate..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedManga != null) {
                MigrationTargetSelection(
                    manga = selectedManga!!,
                    onMigrate = { },
                    onCancel = { selectedManga = null }
                )
            } else {
                EmptyState(
                    icon = { Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(64.dp)) },
                    title = "Search for manga",
                    subtitle = "Find manga to migrate to another source"
                )
            }
        }
    }
}

@Composable
fun MigrationTargetSelection(
    manga: com.kazeyomi.domain.model.Manga,
    onMigrate: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Selected Manga",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = manga.title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Current source: ${manga.sourceId}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onMigrate) {
                    Text("Select Target Source")
                }
            }
        }
    }
}
