package com.kazeyomi.ui.navigation

sealed class Screen(val route: String) {
    object Library : Screen("library")
    object Updates : Screen("updates")
    object Browse : Screen("browse")
    object More : Screen("more")
    object Settings : Screen("settings")
    object History : Screen("history")
    object Downloads : Screen("downloads")
    object Extensions : Screen("extensions")
    object Sources : Screen("sources")
    object GlobalSearch : Screen("global_search")
    object Migration : Screen("migration")
    object About : Screen("about")
    object ServerSetup : Screen("server_setup")
    object BackupRestore : Screen("backup_restore")
    object AppearanceSettings : Screen("appearance_settings")
    object ReaderSettings : Screen("reader_settings")
    object BrowseSettings : Screen("browse_settings")
    object DownloadsSettings : Screen("downloads_settings")
    object LibrarySettings : Screen("library_settings")
    object CategoryManagement : Screen("category_management")
    object MangaDetail : Screen("manga/{mangaId}") {
        fun createRoute(mangaId: Int) = "manga/$mangaId"
    }
    object Reader : Screen("reader/{mangaId}/{chapterId}") {
        fun createRoute(mangaId: Int, chapterId: Int) = "reader/$mangaId/$chapterId"
    }
    object SourceBrowse : Screen("source/{sourceId}/{sourceName}") {
        fun createRoute(sourceId: String, sourceName: String = "") =
            "source/$sourceId/${sourceName}"
    }
    object SourcePreferences : Screen("source_prefs/{sourceId}/{sourceName}") {
        fun createRoute(sourceId: String, sourceName: String = "") =
            "source_prefs/$sourceId/${sourceName}"
    }
}
