package com.kazeyomi.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreMenuScreen(
    onHistoryClick: () -> Unit,
    onDownloadsClick: () -> Unit,
    onMigrationClick: () -> Unit,
    onAboutClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("More") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                ListItem(
                    headlineContent = { Text("History") },
                    supportingContent = { Text("View reading history") },
                    leadingContent = { Icon(Icons.Default.History, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable(onClick = onHistoryClick)
                )
                Divider()
            }

            item {
                ListItem(
                    headlineContent = { Text("Downloads") },
                    supportingContent = { Text("Manage downloaded chapters") },
                    leadingContent = { Icon(Icons.Default.Download, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable(onClick = onDownloadsClick)
                )
                Divider()
            }

            item {
                ListItem(
                    headlineContent = { Text("Migration") },
                    supportingContent = { Text("Migrate manga between sources") },
                    leadingContent = { Icon(Icons.Default.SwapHoriz, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable(onClick = onMigrationClick)
                )
                Divider()
            }

            item {
                ListItem(
                    headlineContent = { Text("About") },
                    supportingContent = { Text("App information and server details") },
                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable(onClick = onAboutClick)
                )
                Divider()
            }

            item {
                ListItem(
                    headlineContent = { Text("Settings") },
                    supportingContent = { Text("Configure app preferences") },
                    leadingContent = { Icon(Icons.Default.Settings, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable(onClick = onSettingsClick)
                )
            }
        }
    }
}
