package dev.vivekchib.inshortsapp.domain.usecases.news

import dev.vivekchib.inshortsapp.domain.entity.Article
import dev.vivekchib.inshortsapp.domain.repository.BookmarkRepository
import dev.vivekchib.inshortsapp.domain.usecases.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarkArticlesUseCase @Inject constructor(private val bookmarkRepository: BookmarkRepository) :
    UseCase<Unit, Flow<List<Article>>> {
    override suspend fun invoke(param: Unit): Flow<List<Article>> {
        return bookmarkRepository.getArticles()
    }
}