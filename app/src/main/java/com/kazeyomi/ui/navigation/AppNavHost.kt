package com.kazeyomi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kazeyomi.ui.screens.about.AboutScreen
import com.kazeyomi.ui.screens.browse.BrowseScreen
import com.kazeyomi.ui.screens.browse.ExtensionsScreen
import com.kazeyomi.ui.screens.browse.GlobalSearchScreen
import com.kazeyomi.ui.screens.browse.SourcesScreen
import com.kazeyomi.ui.screens.downloads.DownloadsScreen
import com.kazeyomi.ui.screens.history.HistoryScreen
import com.kazeyomi.ui.screens.library.LibraryScreen
import com.kazeyomi.ui.screens.manga.MangaDetailScreen
import com.kazeyomi.ui.screens.migration.MigrationScreen
import com.kazeyomi.ui.screens.reader.ReaderScreen
import com.kazeyomi.ui.screens.settings.SettingsScreen
import com.kazeyomi.ui.screens.settings.ServerSetupScreen
import com.kazeyomi.ui.screens.updates.UpdatesScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Library.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Library.route) {
            LibraryScreen(
                onMangaClick = { mangaId ->
                    navController.navigate(Screen.MangaDetail.createRoute(mangaId))
                }
            )
        }

        composable(Screen.Updates.route) {
            UpdatesScreen(
                onMangaClick = { mangaId ->
                    navController.navigate(Screen.MangaDetail.createRoute(mangaId))
                }
            )
        }

        composable(Screen.Browse.route) {
            BrowseScreen(
                onSourceClick = { sourceId ->
                    navController.navigate(Screen.SourceBrowse.createRoute(sourceId))
                },
                onExtensionsClick = { navController.navigate(Screen.Extensions.route) },
                onSourcesClick = { navController.navigate(Screen.Sources.route) },
                onGlobalSearchClick = { navController.navigate(Screen.GlobalSearch.route) }
            )
        }

        composable(Screen.More.route) {
            MoreMenuScreen(
                onHistoryClick = { navController.navigate(Screen.History.route) },
                onDownloadsClick = { navController.navigate(Screen.Downloads.route) },
                onMigrationClick = { navController.navigate(Screen.Migration.route) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onServerSetupClick = { navController.navigate(Screen.ServerSetup.route) }
            )
        }

        composable(Screen.ServerSetup.route) {
            ServerSetupScreen(
                onBackClick = { navController.popBackStack() },
                onConnected = { navController.popBackStack() }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() },
                onMangaClick = { mangaId ->
                    navController.navigate(Screen.MangaDetail.createRoute(mangaId))
                }
            )
        }

        composable(Screen.Downloads.route) {
            DownloadsScreen(
                onBackClick = { navController.popBackStack() },
                onMangaClick = { mangaId ->
                    navController.navigate(Screen.MangaDetail.createRoute(mangaId))
                }
            )
        }

        composable(Screen.Extensions.route) {
            ExtensionsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Sources.route) {
            SourcesScreen(
                onBackClick = { navController.popBackStack() },
                onSourceClick = { sourceId ->
                    navController.navigate(Screen.SourceBrowse.createRoute(sourceId))
                }
            )
        }

        composable(Screen.GlobalSearch.route) {
            GlobalSearchScreen(
                onBackClick = { navController.popBackStack() },
                onMangaClick = { mangaId ->
                    navController.navigate(Screen.MangaDetail.createRoute(mangaId))
                }
            )
        }

        composable(Screen.Migration.route) {
            MigrationScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.About.route) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.MangaDetail.route,
            arguments = listOf(navArgument("mangaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getInt("mangaId") ?: return@composable
            MangaDetailScreen(
                mangaId = mangaId,
                onBackClick = { navController.popBackStack() },
                onChapterClick = { chapterId ->
                    navController.navigate(Screen.Reader.createRoute(mangaId, chapterId))
                }
            )
        }

        composable(
            route = Screen.Reader.route,
            arguments = listOf(
                navArgument("mangaId") { type = NavType.IntType },
                navArgument("chapterId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getInt("mangaId") ?: return@composable
            val chapterId = backStackEntry.arguments?.getInt("chapterId") ?: return@composable
            ReaderScreen(
                mangaId = mangaId,
                initialChapterId = chapterId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
