package com.kazeyomi.ui.screens.settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupRestoreScreen(onBackClick: () -> Unit, viewModel: BackupRestoreViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var showRestoreConfirm by remember { mutableStateOf(false) }

    val createLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { uri -> uri?.let { viewModel.createBackup(it) } }
    val restoreLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> uri?.let { viewModel.restoreBackup(it) } }

    Scaffold(topBar = { TopAppBar(title = { Text("Backup & Restore") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Backup, null); Spacer(Modifier.width(8.dp)); Text("Create Backup", style = MaterialTheme.typography.titleMedium) }
                Text("Save library data to a file", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Button(onClick = { createLauncher.launch("kazeyomi_backup.proto.gz") }, enabled = !state.working, modifier = Modifier.align(Alignment.End)) { Text("Create Backup") }
            }}
            Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Restore, null); Spacer(Modifier.width(8.dp)); Text("Restore Backup", style = MaterialTheme.typography.titleMedium) }
                Text("Restore from a backup file", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Button(onClick = { showRestoreConfirm = true }, enabled = !state.working, modifier = Modifier.align(Alignment.End)) { Text("Restore") }
            }}
            state.message?.let { msg ->
                Card(colors = CardDefaults.cardColors(containerColor = if (state.isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer)) { Text(msg, Modifier.padding(16.dp), color = if (state.isError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer) }
            }
        }
    }
    if (showRestoreConfirm) AlertDialog(onDismissRequest = { showRestoreConfirm = false }, title = { Text("Restore Backup") }, text = { Text("This will overwrite current data. Continue?") }, confirmButton = { TextButton(onClick = { showRestoreConfirm = false; restoreLauncher.launch(arrayOf("application/*", "*/*")) }) { Text("Continue", color = MaterialTheme.colorScheme.error) } }, dismissButton = { TextButton(onClick = { showRestoreConfirm = false }) { Text("Cancel") } })
}
