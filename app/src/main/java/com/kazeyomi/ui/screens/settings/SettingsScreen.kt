package com.kazeyomi.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onServerSetupClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SectionHeader(title = "Server")
            }
            item {
                ListItem(
                    headlineContent = { Text("Server Connection") },
                    supportingContent = { Text(uiState.serverUrl.ifEmpty { "Not configured" }) },
                    leadingContent = { Icon(Icons.Default.Cloud, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable(onClick = onServerSetupClick)
                )
                HorizontalDivider()
            }

            item {
                SectionHeader(title = "Appearance")
            }
            item {
                SwitchSetting(
                    title = "Dynamic Colors",
                    subtitle = "Use Material You theming",
                    checked = uiState.dynamicColor,
                    onCheckedChange = { viewModel.setDynamicColor(it) }
                )
                HorizontalDivider()
            }
            item {
                DropdownSetting(
                    title = "Theme",
                    selectedValue = uiState.themeMode,
                    options = listOf("system", "light", "dark"),
                    onValueChange = { viewModel.setThemeMode(it) }
                )
                HorizontalDivider()
            }
            item {
                DropdownSetting(
                    title = "Language",
                    selectedValue = uiState.language,
                    options = listOf("en", "ja", "ko", "zh"),
                    onValueChange = { viewModel.setLanguage(it) }
                )
                HorizontalDivider()
            }

            item {
                SectionHeader(title = "Library")
            }
            item {
                DropdownSetting(
                    title = "Display Mode",
                    selectedValue = uiState.libraryDisplayMode,
                    options = listOf("grid", "list"),
                    onValueChange = { viewModel.setLibraryDisplayMode(it) }
                )
                HorizontalDivider()
            }
            item {
                SwitchSetting(
                    title = "Auto Update Library",
                    subtitle = "Check for updates periodically",
                    checked = uiState.autoUpdateLibrary,
                    onCheckedChange = { viewModel.setAutoUpdateLibrary(it) }
                )
                HorizontalDivider()
            }

            item {
                SectionHeader(title = "Reader")
            }
            item {
                DropdownSetting(
                    title = "Reading Mode",
                    selectedValue = uiState.readerMode,
                    options = listOf("VERTICAL", "HORIZONTAL_SINGLE", "HORIZONTAL_CONTINUOUS", "WEBTOON"),
                    onValueChange = { viewModel.setReaderMode(it) }
                )
                HorizontalDivider()
            }
            item {
                DropdownSetting(
                    title = "Reading Direction",
                    selectedValue = uiState.readerDirection,
                    options = listOf("LEFT_TO_RIGHT", "RIGHT_TO_LEFT"),
                    onValueChange = { viewModel.setReaderDirection(it) }
                )
                HorizontalDivider()
            }
            item {
                SwitchSetting(
                    title = "Show Page Number",
                    checked = uiState.readerShowPageNum,
                    onCheckedChange = { viewModel.setReaderShowPageNum(it) }
                )
                HorizontalDivider()
            }
            item {
                SwitchSetting(
                    title = "Volume Keys",
                    subtitle = "Use volume buttons to turn pages",
                    checked = uiState.readerVolumeKeys,
                    onCheckedChange = { viewModel.setReaderVolumeKeys(it) }
                )
                HorizontalDivider()
            }
            item {
                SwitchSetting(
                    title = "Keep Screen On",
                    checked = uiState.readerKeepScreen,
                    onCheckedChange = { viewModel.setReaderKeepScreen(it) }
                )
                HorizontalDivider()
            }
            item {
                SwitchSetting(
                    title = "Auto Mark as Read",
                    checked = uiState.readerAutoMark,
                    onCheckedChange = { viewModel.setReaderAutoMark(it) }
                )
                HorizontalDivider()
            }

            item {
                SectionHeader(title = "Downloads")
            }
            item {
                SwitchSetting(
                    title = "Download as CBZ",
                    subtitle = "Package downloads as comic book archives",
                    checked = uiState.downloadAsCbz,
                    onCheckedChange = { viewModel.setDownloadAsCbz(it) }
                )
                HorizontalDivider()
            }
            item {
                DropdownSetting(
                    title = "Concurrent Downloads",
                    selectedValue = uiState.downloadChapters.toString(),
                    options = listOf("1", "2", "3", "5", "10"),
                    onValueChange = { viewModel.setDownloadChapters(it.toInt()) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun DropdownSetting(
    title: String,
    selectedValue: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(selectedValue.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }) },
        trailingContent = {
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                onValueChange(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    )
}
