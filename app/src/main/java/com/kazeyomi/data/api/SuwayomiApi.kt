package com.kazeyomi.data.api

import retrofit2.http.*

interface SuwayomiApi {
    @GET("api/v1/manga/{mangaId}")
    suspend fun getManga(@Path("mangaId") mangaId: Int): MangaResponse

    @GET("api/v1/manga/{mangaId}/chapters")
    suspend fun getChapters(
        @Path("mangaId") mangaId: Int,
        @Query("order") order: String? = null
    ): ChaptersResponse

    @GET("api/v1/chapter/{chapterId}/pages")
    suspend fun getChapterPages(@Path("chapterId") chapterId: Int): ChapterPagesResponse

    @POST("api/v1/manga/{mangaId}/read/{chapterId}")
    suspend fun markChapterAsRead(
        @Path("mangaId") mangaId: Int,
        @Path("chapterId") chapterId: Int,
        @Query("read") read: Boolean = true
    )

    @POST("api/v1/manga/{mangaId}/bookmark/{chapterId}")
    suspend fun toggleBookmark(
        @Path("mangaId") mangaId: Int,
        @Path("chapterId") chapterId: Int,
        @Query("bookmark") bookmark: Boolean
    )

    @POST("api/v1/manga/{mangaId}")
    suspend fun updateManga(
        @Path("mangaId") mangaId: Int,
        @Body update: Map<String, Any>
    )

    @POST("api/v1/manga/{mangaId}/fetch")
    suspend fun fetchNewChapters(@Path("mangaId") mangaId: Int)

    @GET("api/v1/library")
    suspend fun getLibrary(
        @Query("categoryId") categoryId: Int? = null
    ): MangasResponse

    @GET("api/v1/category")
    suspend fun getCategories(): List<CategoryDto>

    @POST("api/v1/category")
    suspend fun createCategory(@Body category: CategoryDto): CategoryDto

    @PUT("api/v1/category/{categoryId}")
    suspend fun updateCategory(
        @Path("categoryId") categoryId: Int,
        @Body category: CategoryDto
    )

    @DELETE("api/v1/category/{categoryId}")
    suspend fun deleteCategory(@Path("categoryId") categoryId: Int)

    @POST("api/v1/manga/{mangaId}/category")
    suspend fun addMangaToCategory(
        @Path("mangaId") mangaId: Int,
        @Body categoryIds: List<Int>
    )

    @GET("api/v1/sources")
    suspend fun getSources(
        @Query("installed") installed: Boolean? = null
    ): SourcesResponse

    @GET("api/v1/source/{sourceId}/search")
    suspend fun searchSource(
        @Path("sourceId") sourceId: String,
        @Query("search") query: String
    ): MangasResponse

    @GET("api/v1/source/{sourceId}/popular/{page}")
    suspend fun getPopularManga(
        @Path("sourceId") sourceId: String,
        @Path("page") page: Int = 1
    ): MangasResponse

    @GET("api/v1/source/{sourceId}/latest/{page}")
    suspend fun getLatestManga(
        @Path("sourceId") sourceId: String,
        @Path("page") page: Int = 1
    ): MangasResponse

    @GET("api/v1/extensions")
    suspend fun getExtensions(): ExtensionsResponse

    @POST("api/v1/extension/{pkgName}/install")
    suspend fun installExtension(@Path("pkgName") pkgName: String)

    @POST("api/v1/extension/{pkgName}/uninstall")
    suspend fun uninstallExtension(@Path("pkgName") pkgName: String)

    @POST("api/v1/extension/{pkgName}/update")
    suspend fun updateExtension(@Path("pkgName") pkgName: String)

    @GET("api/v1/download")
    suspend fun getDownloads(): DownloadsResponse

    @POST("api/v1/download/{chapterId}")
    suspend fun enqueueDownload(@Path("chapterId") chapterId: Int)

    @DELETE("api/v1/download/{chapterId}")
    suspend fun cancelDownload(@Path("chapterId") chapterId: Int)

    @POST("api/v1/download")
    suspend fun clearDownloads()

    @GET("api/v1/history")
    suspend fun getHistory(): HistoryResponse

    @DELETE("api/v1/history/{historyId}")
    suspend fun deleteHistoryItem(@Path("historyId") historyId: Int)

    @DELETE("api/v1/history")
    suspend fun clearHistory()

    @POST("api/v1/history/{mangaId}/{chapterId}")
    suspend fun setReadHistory(
        @Path("mangaId") mangaId: Int,
        @Path("chapterId") chapterId: Int,
        @Query("lastPage") lastPage: Int
    )

    @GET("api/v1/about")
    suspend fun getAboutServer(): AboutServerResponse

    @GET("api/v1/globalSearch")
    suspend fun globalSearch(@Query("search") query: String): GlobalSearchResponse

    @GET("api/v1/updates")
    suspend fun getUpdates(
        @Query("since") since: Long? = null
    ): UpdatesResponse
}
