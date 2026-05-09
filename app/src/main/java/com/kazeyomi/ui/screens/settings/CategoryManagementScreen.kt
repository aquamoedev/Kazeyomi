package com.kazeyomi.ui.screens.settings
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
import com.kazeyomi.domain.model.Category
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(onBackClick: () -> Unit, viewModel: com.kazeyomi.ui.screens.library.CategoryManagementViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var showCreate by remember { mutableStateOf(false) }
    var showEdit by remember { mutableStateOf<Category?>(null) }
    var showDelete by remember { mutableStateOf<Category?>(null) }
    LaunchedEffect(Unit) { viewModel.loadCategories() }

    Scaffold(topBar = { TopAppBar(title = { Text("Categories") }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }, actions = { IconButton(onClick = { showCreate = true }) { Icon(Icons.Default.Add, "Add") } }) }) { padding ->
        when {
            state.isLoading -> LoadingIndicator(Modifier.padding(padding))
            state.error != null -> ErrorMessage(state.error!!, { viewModel.loadCategories() }, Modifier.padding(padding))
            else -> LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                items(state.categories, key = { it.id }) { cat ->
                    ListItem(headlineContent = { Text(cat.name) }, supportingContent = { Text("${cat.size} manga") }, trailingContent = { Row { IconButton(onClick = { showEdit = cat }) { Icon(Icons.Default.Edit, "Edit") }; if (!cat.default) IconButton(onClick = { showDelete = cat }) { Icon(Icons.Default.Delete, "Delete") } } })
                    Divider()
                }
            }
        }
    }
    if (showCreate) { var n by remember { mutableStateOf("") }; AlertDialog(onDismissRequest = { showCreate = false }, title = { Text("New Category") }, text = { OutlinedTextField(value = n, onValueChange = { n = it }, label = { Text("Name") }, singleLine = true) }, confirmButton = { TextButton(onClick = { if (n.isNotBlank()) viewModel.createCategory(n); showCreate = false }) { Text("Create") } }, dismissButton = { TextButton(onClick = { showCreate = false }) { Text("Cancel") } }) }
    showEdit?.let { cat -> var n by remember { mutableStateOf(cat.name) }; AlertDialog(onDismissRequest = { showEdit = null }, title = { Text("Edit Category") }, text = { OutlinedTextField(value = n, onValueChange = { n = it }, label = { Text("Name") }, singleLine = true) }, confirmButton = { TextButton(onClick = { viewModel.updateCategory(cat.id, n); showEdit = null }) { Text("Save") } }, dismissButton = { TextButton(onClick = { showEdit = null }) { Text("Cancel") } }) }
    showDelete?.let { cat -> AlertDialog(onDismissRequest = { showDelete = null }, title = { Text("Delete Category") }, text = { Text("Delete '${cat.name}'? Manga won't be deleted.") }, confirmButton = { TextButton(onClick = { viewModel.deleteCategory(cat.id); showDelete = null }) { Text("Delete", color = MaterialTheme.colorScheme.error) } }, dismissButton = { TextButton(onClick = { showDelete = null }) { Text("Cancel") } }) }

    // Use the CategoryManagementViewModel from the library package
    // Actually we need to create it here properly
}
