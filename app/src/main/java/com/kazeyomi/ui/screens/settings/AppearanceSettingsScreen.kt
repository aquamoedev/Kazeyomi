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
fun AppearanceSettingsScreen(onBackClick: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val s by viewModel.appearance.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Appearance") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Theme", style = MaterialTheme.typography.titleMedium)
            listOf("System Default", "Light", "Dark").forEachIndexed { i, t ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(t); RadioButton(selected = s.themeIndex == i, onClick = { viewModel.setTheme(i) }) }
            }
            Divider(Modifier.padding(vertical = 8.dp))
            Text("Grid Cover Width: ${s.gridCoverWidth}dp", style = MaterialTheme.typography.titleMedium)
            Slider(value = s.gridCoverWidth.toFloat(), onValueChange = { viewModel.setGridCoverWidth(it.toInt()) }, valueRange = 100f..200f, modifier = Modifier.fillMaxWidth())
            Divider(Modifier.padding(vertical = 8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text("True Black (AMOLED)"); Switch(checked = s.isTrueBlack, onCheckedChange = { viewModel.setTrueBlack(it) }) }
        }
    }
}
