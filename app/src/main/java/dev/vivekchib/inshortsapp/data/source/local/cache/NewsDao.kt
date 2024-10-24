package dev.vivekchib.inshortsapp.data.source.local.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.vivekchib.inshortsapp.data.model.ArticleModel

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleModel)

    @Query("DELETE FROM articles")
    suspend fun deleteArticles()

    @Query("SELECT * FROM articles WHERE timestamp BETWEEN :from AND :to")
    suspend fun getArticles(from: Long, to: Long): List<ArticleModel>
}