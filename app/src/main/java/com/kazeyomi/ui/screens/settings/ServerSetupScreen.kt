package com.kazeyomi.ui.screens.settings
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
fun ServerSetupScreen(onBackClick: () -> Unit, onConnected: () -> Unit = {}, viewModel: ServerSetupViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var url by remember { mutableStateOf("http://") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(state.isConnected) { if (state.isConnected) onConnected() }

    Scaffold(topBar = { TopAppBar(title = { Text("Server Connection") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("Server URL") }, modifier = Modifier.fillMaxWidth(), singleLine = true, leadingIcon = { Icon(Icons.Default.Cloud, null) })
            OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username (optional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password (optional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation())
            Button(onClick = { viewModel.connect(url, username.ifBlank { null }, password.ifBlank { null }) }, enabled = !state.isConnecting, modifier = Modifier.fillMaxWidth()) {
                if (state.isConnecting) { CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp); Spacer(Modifier.width(8.dp)) }
                Text("Connect")
            }
            state.error?.let { Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) { Text(it, Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onErrorContainer) } }
            state.serverInfo?.let { s ->
                Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { Text("Connected!", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary); Spacer(Modifier.height(4.dp)); Text("Server: ${s.versionName}"); Text("API: v${s.apiVersion}") } }
            }
        }
    }
}
