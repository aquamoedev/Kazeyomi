package com.kazeyomi.ui.navigation

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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

data class BottomNavItem(val route: String, val label: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector)

@Composable
fun KazeyomiApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val current = navBackStackEntry?.destination

    val tabs = listOf(
        BottomNavItem(Screen.Library.route, "Library", Icons.Filled.Book, Icons.Outlined.Book),
        BottomNavItem(Screen.Updates.route, "Updates", Icons.Filled.Update, Icons.Outlined.Update),
        BottomNavItem(Screen.Browse.route, "Browse", Icons.Filled.Explore, Icons.Outlined.Explore),
        BottomNavItem(Screen.More.route, "More", Icons.Filled.MoreHoriz, Icons.Outlined.MoreHoriz)
    )
    val showBottomBar = current?.route in tabs.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) NavigationBar {
                tabs.forEach { item ->
                    val selected = current?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(selected = selected, onClick = {
                        navController.navigate(item.route) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true }
                    }, icon = { Icon(if (selected) item.selectedIcon else item.unselectedIcon, item.label) }, label = { Text(item.label) })
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) { AppNavHost(navController) }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Library.route) {
        composable(Screen.Library.route) { com.kazeyomi.ui.screens.library.LibraryScreen(onMangaClick = { navController.navigate(Screen.MangaDetail.createRoute(it)) }) }
        composable(Screen.Updates.route) { com.kazeyomi.ui.screens.updates.UpdatesScreen(onMangaClick = { navController.navigate(Screen.MangaDetail.createRoute(it)) }) }
        composable(Screen.Browse.route) {
            com.kazeyomi.ui.screens.browse.BrowseScreen(
                onGlobalSearchClick = { navController.navigate(Screen.GlobalSearch.route) },
                onExtensionsClick = { navController.navigate(Screen.Extensions.route) },
                onSourcesClick = { navController.navigate(Screen.Sources.route) },
                onSourceClick = { id, name -> navController.navigate(Screen.SourceBrowse.createRoute(id, name)) }
            )
        }
        composable(Screen.More.route) {
            com.kazeyomi.ui.screens.MoreMenuScreen(
                onHistoryClick = { navController.navigate(Screen.History.route) },
                onDownloadsClick = { navController.navigate(Screen.Downloads.route) },
                onMigrationClick = { navController.navigate(Screen.Migration.route) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Settings.route) {
            com.kazeyomi.ui.screens.settings.SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onServerSetupClick = { navController.navigate(Screen.ServerSetup.route) },
                onReaderSettingsClick = { navController.navigate(Screen.ReaderSettings.route) },
                onAppearanceClick = { navController.navigate(Screen.AppearanceSettings.route) },
                onBrowseSettingsClick = { navController.navigate(Screen.BrowseSettings.route) },
                onDownloadsSettingsClick = { navController.navigate(Screen.DownloadsSettings.route) },
                onLibrarySettingsClick = { navController.navigate(Screen.LibrarySettings.route) },
                onBackupRestoreClick = { navController.navigate(Screen.BackupRestore.route) },
                onCategoryManagementClick = { navController.navigate(Screen.CategoryManagement.route) }
            )
        }
        composable(Screen.ServerSetup.route) { com.kazeyomi.ui.screens.settings.ServerSetupScreen(onBackClick = { navController.popBackStack() }, onConnected = { navController.popBackStack() }) }
        composable(Screen.AppearanceSettings.route) { com.kazeyomi.ui.screens.settings.AppearanceSettingsScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.BrowseSettings.route) { com.kazeyomi.ui.screens.settings.BrowseSettingsScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.DownloadsSettings.route) { com.kazeyomi.ui.screens.settings.DownloadsSettingsScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.LibrarySettings.route) { com.kazeyomi.ui.screens.settings.LibrarySettingsScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.ReaderSettings.route) { com.kazeyomi.ui.screens.settings.ReaderSettingsScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.BackupRestore.route) { com.kazeyomi.ui.screens.settings.BackupRestoreScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.CategoryManagement.route) { com.kazeyomi.ui.screens.settings.CategoryManagementScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.History.route) { com.kazeyomi.ui.screens.history.HistoryScreen(onBackClick = { navController.popBackStack() }, onMangaClick = { navController.navigate(Screen.MangaDetail.createRoute(it)) }) }
        composable(Screen.Downloads.route) { com.kazeyomi.ui.screens.downloads.DownloadsScreen(onBackClick = { navController.popBackStack() }, onMangaClick = { navController.navigate(Screen.MangaDetail.createRoute(it)) }) }
        composable(Screen.Extensions.route) { com.kazeyomi.ui.screens.browse.ExtensionsScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.Sources.route) { com.kazeyomi.ui.screens.browse.SourcesScreen(onBackClick = { navController.popBackStack() }, onSourceClick = { id, name -> navController.navigate(Screen.SourceBrowse.createRoute(id, name)) }) }
        composable(Screen.GlobalSearch.route) { com.kazeyomi.ui.screens.browse.GlobalSearchScreen(onBackClick = { navController.popBackStack() }, onMangaClick = { navController.navigate(Screen.MangaDetail.createRoute(it)) }) }
        composable(Screen.Migration.route) { com.kazeyomi.ui.screens.migration.MigrationScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.About.route) { com.kazeyomi.ui.screens.about.AboutScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.MangaDetail.route, arguments = listOf(navArgument("mangaId") { type = NavType.IntType })) { back ->
            val id = back.arguments?.getInt("mangaId") ?: return@composable
            com.kazeyomi.ui.screens.manga.MangaDetailScreen(mangaId = id, onBackClick = { navController.popBackStack() }, onChapterClick = { chId -> navController.navigate(Screen.Reader.createRoute(id, chId)) })
        }
        composable(Screen.Reader.route, arguments = listOf(navArgument("mangaId") { type = NavType.IntType }, navArgument("chapterId") { type = NavType.IntType })) { back ->
            val mid = back.arguments?.getInt("mangaId") ?: return@composable
            val cid = back.arguments?.getInt("chapterId") ?: return@composable
            com.kazeyomi.ui.screens.reader.ReaderScreen(mangaId = mid, initialChapterId = cid, onBackClick = { navController.popBackStack() })
        }
        composable(Screen.SourceBrowse.route, arguments = listOf(navArgument("sourceId") { type = NavType.StringType }, navArgument("sourceName") { type = NavType.StringType; defaultValue = "" })) { back ->
            val sid = back.arguments?.getString("sourceId") ?: return@composable
            val sname = back.arguments?.getString("sourceName") ?: ""
            com.kazeyomi.ui.screens.browse.SourceBrowseScreen(sourceId = sid, sourceName = sname, onBackClick = { navController.popBackStack() }, onMangaClick = { navController.navigate(Screen.MangaDetail.createRoute(it)) })
        }
        composable(Screen.SourcePreferences.route, arguments = listOf(navArgument("sourceId") { type = NavType.StringType }, navArgument("sourceName") { type = NavType.StringType; defaultValue = "" })) { back ->
            val sid = back.arguments?.getString("sourceId") ?: return@composable
            val sname = back.arguments?.getString("sourceName") ?: ""
            com.kazeyomi.ui.screens.browse.SourcePreferencesScreen(sourceId = sid, sourceName = sname, onBackClick = { navController.popBackStack() })
        }
    }
}
