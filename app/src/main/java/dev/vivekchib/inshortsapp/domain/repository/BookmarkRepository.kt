package dev.vivekchib.inshortsapp.domain.repository

import dev.vivekchib.inshortsapp.domain.entity.Article
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun insertArticle(article: Article): Result<Unit>
    suspend fun deleteArticle(article: Article): Result<Unit>
    fun getArticles(): Flow<List<Article>>
}