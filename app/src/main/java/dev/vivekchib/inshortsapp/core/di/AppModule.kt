package dev.vivekchib.inshortsapp.core.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.vivekchib.inshortsapp.core.constants.API
import dev.vivekchib.inshortsapp.data.repository.BookmarkRepoImpl
import dev.vivekchib.inshortsapp.data.repository.NewsRepoImpl
import dev.vivekchib.inshortsapp.data.source.local.app.AppDatabase
import dev.vivekchib.inshortsapp.data.source.local.cache.CacheDatabase
import dev.vivekchib.inshortsapp.data.source.remote.NewsApi
import dev.vivekchib.inshortsapp.domain.repository.BookmarkRepository
import dev.vivekchib.inshortsapp.domain.repository.NewsRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "app-database.db").build()
    }

    // CACHE Database
    @Provides
    @Singleton
    fun provideCacheDatabase(app: Application): CacheDatabase {
        return Room.databaseBuilder(
            app, CacheDatabase::class.java, "inshorts.db"
        ).build()
    }

    // API SERVICE
    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return Retrofit
            .Builder()
            .baseUrl(API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NewsApi::class.java)
    }

    // News Repository
    @Provides
    @Singleton
    fun provideNewsRepository(newsApi: NewsApi, cacheDB: CacheDatabase,appDB : AppDatabase): NewsRepository {
        return NewsRepoImpl(newsApi = newsApi, newsDao = cacheDB.articleDao(), bookmarkDao = appDB.bookmarkDao)
    }

    // Bookmark Repository
    @Provides
    @Singleton
    fun provideBookmarkRepository(appDB : AppDatabase): BookmarkRepository {
        return BookmarkRepoImpl(appDB.bookmarkDao)
    }

}