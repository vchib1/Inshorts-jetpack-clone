package dev.vivekchib.inshortsapp.domain.repository

import dev.vivekchib.inshortsapp.domain.entity.Article

interface NewsRepository {
    suspend fun getTopHeadlines(country: String): Result<List<Article>>
}