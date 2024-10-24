package dev.vivekchib.inshortsapp.domain.usecases.news

import dev.vivekchib.inshortsapp.domain.entity.Article
import dev.vivekchib.inshortsapp.domain.repository.BookmarkRepository
import dev.vivekchib.inshortsapp.domain.usecases.UseCase
import javax.inject.Inject

class RemoveBookmarkArticleUseCase @Inject constructor(private val bookmarkRepo: BookmarkRepository) :
    UseCase<Article, Result<Unit>> {
    override suspend fun invoke(param: Article): Result<Unit> {
        return bookmarkRepo.deleteArticle(param)
    }
}