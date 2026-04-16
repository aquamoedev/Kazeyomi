package com.kazeyomi.ui.screens.browse

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
import com.kazeyomi.domain.model.Extension
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtensionsScreen(
    onBackClick: () -> Unit,
    viewModel: ExtensionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadExtensions()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Extensions") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadExtensions() }) {
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
                onRetry = { viewModel.loadExtensions() },
                modifier = Modifier.padding(paddingValues)
            )
            uiState.extensions.isEmpty() -> EmptyState(
                icon = { Icon(Icons.Default.Extension, contentDescription = null, modifier = Modifier.size(64.dp)) },
                title = "No extensions available",
                modifier = Modifier.padding(paddingValues)
            )
            else -> ExtensionsList(
                extensions = uiState.extensions,
                onInstall = { viewModel.installExtension(it) },
                onUninstall = { viewModel.uninstallExtension(it) },
                onUpdate = { viewModel.updateExtension(it) },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun ExtensionsList(
    extensions: List<Extension>,
    onInstall: (String) -> Unit,
    onUninstall: (String) -> Unit,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(extensions, key = { it.pkgName }) { extension ->
            ExtensionItem(
                extension = extension,
                onInstall = { onInstall(extension.pkgName) },
                onUninstall = { onUninstall(extension.pkgName) },
                onUpdate = { onUpdate(extension.pkgName) }
            )
        }
    }
}

@Composable
fun ExtensionItem(
    extension: Extension,
    onInstall: () -> Unit,
    onUninstall: () -> Unit,
    onUpdate: () -> Unit
) {
    ListItem(
        headlineContent = { Text(extension.name) },
        supportingContent = { Text("v${extension.version} • ${extension.lang}") },
        leadingContent = {
            Icon(
                Icons.Default.Extension,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        },
        trailingContent = {
            when (extension.installButton) {
                com.kazeyomi.domain.model.InstallButton.INSTALL -> {
                    FilledTonalButton(onClick = onInstall) { Text("Install") }
                }
                com.kazeyomi.domain.model.InstallButton.UPDATE -> {
                    FilledTonalButton(onClick = onUpdate) { Text("Update") }
                }
                com.kazeyomi.domain.model.InstallButton.UNINSTALL -> {
                    OutlinedButton(onClick = onUninstall) { Text("Uninstall") }
                }
                com.kazeyomi.domain.model.InstallButton.INSTALLED -> {
                    AssistChip(
                        onClick = {},
                        label = { Text("Installed") },
                        leadingIcon = { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                }
            }
        },
        modifier = Modifier.clickable {}
    )
    Divider()
}
