package com.kazeyomi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kazeyomi.ui.navigation.AppNavHost
import com.kazeyomi.ui.navigation.Screen

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun KazeyomiApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Library.route, "Library", Icons.Filled.Book, Icons.Outlined.Book),
        BottomNavItem(Screen.Updates.route, "Updates", Icons.Filled.Update, Icons.Outlined.Update),
        BottomNavItem(Screen.Browse.route, "Browse", Icons.Filled.Explore, Icons.Outlined.Explore),
        BottomNavItem(Screen.More.route, "More", Icons.Filled.MoreHoriz, Icons.Outlined.MoreHoriz)
    )

    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavHost(navController = navController)
        }
    }
}

@Composable
fun MoreMenuScreen(
    onHistoryClick: () -> Unit,
    onDownloadsClick: () -> Unit,
    onMigrationClick: () -> Unit,
    onAboutClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("More") },
            text = {
                Column {
                    ListItem(
                        headlineContent = { Text("History") },
                        leadingContent = { Icon(Icons.Default.History, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider()
                    ListItem(
                        headlineContent = { Text("Downloads") },
                        leadingContent = { Icon(Icons.Default.Download, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider()
                    ListItem(
                        headlineContent = { Text("Migration") },
                        leadingContent = { Icon(Icons.Default.SwapHoriz, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider()
                    ListItem(
                        headlineContent = { Text("About") },
                        leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider()
                    ListItem(
                        headlineContent = { Text("Settings") },
                        leadingContent = { Icon(Icons.Default.Settings, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        if (showDialog) {
            kotlinx.coroutines.delay(100)
            showDialog = false
        }
    }
}
