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
    object MangaDetail : Screen("manga/{mangaId}") {
        fun createRoute(mangaId: Int) = "manga/$mangaId"
    }
    object Reader : Screen("reader/{mangaId}/{chapterId}") {
        fun createRoute(mangaId: Int, chapterId: Int) = "reader/$mangaId/$chapterId"
    }
    object SourceBrowse : Screen("source/{sourceId}") {
        fun createRoute(sourceId: String) = "source/$sourceId"
    }
    object SourceSearch : Screen("source/{sourceId}/search") {
        fun createRoute(sourceId: String) = "source/$sourceId/search"
    }
    object ServerSetup : Screen("server_setup")
}
