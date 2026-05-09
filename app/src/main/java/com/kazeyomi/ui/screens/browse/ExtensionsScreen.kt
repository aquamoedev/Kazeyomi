package com.kazeyomi.ui.screens.browse
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
import com.kazeyomi.domain.model.Extension
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtensionsScreen(onBackClick: () -> Unit, viewModel: ExtensionsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadExtensions() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Extensions") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }, actions = { IconButton(onClick = { viewModel.loadExtensions() }) { Icon(Icons.Default.Refresh, "Refresh") } }) }
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(Modifier.padding(padding))
            state.error != null -> ErrorMessage(state.error!!, { viewModel.loadExtensions() }, Modifier.padding(padding))
            else -> LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                items(state.extensions, key = { it.pkgName }) { ext ->
                    ListItem(
                        headlineContent = { Text("${ext.name}  v${ext.version}") },
                        supportingContent = { Text("${ext.pkgName}  •  ${ext.lang}") },
                        leadingContent = { Icon(Icons.Default.Extension, null, tint = if (ext.isInstalled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) },
                        trailingContent = {
                            if (!ext.isInstalled) TextButton(onClick = { viewModel.install(ext.pkgName) }) { Text("Install") }
                            else if (ext.hasUpdate) TextButton(onClick = { viewModel.update(ext.pkgName) }) { Text("Update") }
                            else if (ext.isObsolete) TextButton(onClick = { viewModel.uninstall(ext.pkgName) }) { Text("Remove", color = MaterialTheme.colorScheme.error) }
                        }
                    )
                    Divider()
                }
            }
        }
    }
}
