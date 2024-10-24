package dev.vivekchib.inshortsapp.domain.usecases.news

import dev.vivekchib.inshortsapp.domain.entity.Article
import dev.vivekchib.inshortsapp.domain.repository.NewsRepository
import dev.vivekchib.inshortsapp.domain.usecases.UseCase
import jakarta.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(private val newsRepo: NewsRepository) :
    UseCase<String, Result<List<Article>>> {
    override suspend fun invoke(param: String): Result<List<Article>> {
        return newsRepo.getTopHeadlines(country = param)
    }
}