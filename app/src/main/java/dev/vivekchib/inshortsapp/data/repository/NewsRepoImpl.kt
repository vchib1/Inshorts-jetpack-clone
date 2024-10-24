package dev.vivekchib.inshortsapp.data.repository

import android.util.Log
import dev.vivekchib.inshortsapp.data.model.ArticleModel
import dev.vivekchib.inshortsapp.data.model.NewsResponse
import dev.vivekchib.inshortsapp.data.source.local.app.BookmarkDao
import dev.vivekchib.inshortsapp.data.source.local.cache.NewsDao
import dev.vivekchib.inshortsapp.data.source.remote.NewsApi
import dev.vivekchib.inshortsapp.domain.entity.Article
import dev.vivekchib.inshortsapp.domain.repository.NewsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

private const val TAG = "NewsRepoImpl"

class NewsRepoImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao,
    private val bookmarkDao: BookmarkDao
) :
    NewsRepository {

    private val cacheDuration = Duration.ofMinutes(60).toMinutes()

    override suspend fun getTopHeadlines(country: String): Result<List<Article>> = coroutineScope {
        try {
            val now = LocalDateTime.now()
            val cacheStartTime = now.minusMinutes(cacheDuration).toEpochSecond(ZoneOffset.UTC)
            val cacheEndTime = now.toEpochSecond(ZoneOffset.UTC)

            val cachedArticlesDeferred = async { newsDao.getArticles(cacheStartTime, cacheEndTime) }

            val cachedArticles = cachedArticlesDeferred.await().map { it.toEntity() }

            if (cachedArticles.isNotEmpty()) {
                Log.d(TAG, "${cachedArticles.size} articles fetched from cache")
                return@coroutineScope Result.success(cachedArticles)
            }

            // Fetch from remote
            val response: Response<NewsResponse> = newsApi.getTopHeadlines(country)

            if (response.isSuccessful && response.body() != null) {
                val articles = response.body()!!.articles
                    .filter { it.content != "[Removed]" }

                launch(Dispatchers.IO) {
                    newsDao.deleteArticles()
                    newsDao.insertArticles(articles.map { it.addTimestamp() })
                    Log.d(TAG, "${articles.size} articles cached in db")
                }

                Log.d(TAG, "${articles.size} articles fetched from remote")
                return@coroutineScope Result.success(articles.map { it.toEntity() })
            } else {
                return@coroutineScope Result.failure(Exception(response.message()))
            }
        } catch (e: IOException) {
            Log.e(TAG, e.message.toString())
            return@coroutineScope Result.failure(Exception("IOException: $e"))
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            return@coroutineScope Result.failure(Exception("Exception: $e"))
        }
    }
}