package dev.vivekchib.inshortsapp.data.source.local.app

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.vivekchib.inshortsapp.data.model.ArticleModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleModel)

    @Query("DELETE FROM articles WHERE url = :articleUrl")
    suspend fun deleteArticle(articleUrl: String)

    @Query("SELECT * FROM articles")
    fun getArticles(): Flow<List<ArticleModel>>

    @Query("SELECT title FROM articles")
    suspend fun getTitles(): List<String>
}