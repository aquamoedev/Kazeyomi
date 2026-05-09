package com.kazeyomi.di

import android.content.Context
import com.kazeyomi.data.api.ApiClient
import com.kazeyomi.data.local.PreferencesManager
import com.kazeyomi.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiClient(): ApiClient = ApiClient()

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager = PreferencesManager(context)

    @Provides
    @Singleton
    fun provideMangaRepository(apiClient: ApiClient): MangaRepository = MangaRepository(apiClient)

    @Provides
    @Singleton
    fun provideLibraryRepository(apiClient: ApiClient): LibraryRepository = LibraryRepository(apiClient)

    @Provides
    @Singleton
    fun provideBrowseRepository(apiClient: ApiClient): BrowseRepository = BrowseRepository(apiClient)

    @Provides
    @Singleton
    fun provideDownloadRepository(apiClient: ApiClient): DownloadRepository = DownloadRepository(apiClient)

    @Provides
    @Singleton
    fun provideHistoryRepository(apiClient: ApiClient): HistoryRepository = HistoryRepository(apiClient)

    @Provides
    @Singleton
    fun provideServerRepository(apiClient: ApiClient): ServerRepository = ServerRepository(apiClient)
}
