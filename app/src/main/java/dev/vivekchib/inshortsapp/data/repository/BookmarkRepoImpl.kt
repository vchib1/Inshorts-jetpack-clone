package dev.vivekchib.inshortsapp.data.repository

import dev.vivekchib.inshortsapp.data.model.ArticleModel
import dev.vivekchib.inshortsapp.data.source.local.app.BookmarkDao
import dev.vivekchib.inshortsapp.domain.entity.Article
import dev.vivekchib.inshortsapp.domain.repository.BookmarkRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepoImpl @Inject constructor(private val bookmarkDao: BookmarkDao) :
    BookmarkRepository {
    override suspend fun insertArticle(article: Article): Result<Unit> {
        return try {
            bookmarkDao.insertArticle(ArticleModel.fromEntity(article.copy(bookMarked = true)))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteArticle(article: Article): Result<Unit> {
        return try {
            bookmarkDao.deleteArticle(article.url!!)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getArticles(): Flow<List<Article>> {
        return bookmarkDao.getArticles().map { list -> list.map { it.toEntity() } }
    }

}