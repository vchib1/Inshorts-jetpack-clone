package dev.vivekchib.inshortsapp.domain.usecases.news

import dev.vivekchib.inshortsapp.domain.entity.Article
import dev.vivekchib.inshortsapp.domain.repository.BookmarkRepository
import dev.vivekchib.inshortsapp.domain.usecases.UseCase
import javax.inject.Inject

class AddBookmarkArticleUseCase @Inject constructor(private val bookmarkRepository: BookmarkRepository) :
    UseCase<Article, Result<Unit>> {
    override suspend fun invoke(param: Article): Result<Unit> {
        return bookmarkRepository.insertArticle(param)
    }
}