package com.kazeyomi.ui.screens.migration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MigrationScreen(onBackClick: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Migration") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }) }) { padding ->
        EmptyState("Coming Soon", "Manga source migration will be available in a future update", modifier = Modifier.padding(padding))
    }
}
