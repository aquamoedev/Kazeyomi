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
import com.kazeyomi.domain.model.ReadingMode
import com.kazeyomi.domain.model.ReadingDirection
import com.kazeyomi.domain.model.TapZones

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsScreen(onBackClick: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val s by viewModel.state.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Reader Settings") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Reading Mode", style = MaterialTheme.typography.titleMedium)
            ReadingMode.entries.forEach { m -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(m.name); RadioButton(selected = s.readerMode == m.name, onClick = {}) } }
            Divider(Modifier.padding(vertical = 8.dp))
            Text("Reading Direction", style = MaterialTheme.typography.titleMedium)
            ReadingDirection.entries.forEach { d -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(d.name); RadioButton(selected = false, onClick = {}) } }
        }
    }
}
