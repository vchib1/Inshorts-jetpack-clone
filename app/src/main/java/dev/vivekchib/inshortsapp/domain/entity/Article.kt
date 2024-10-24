package dev.vivekchib.inshortsapp.domain.entity

data class Article(
    val sourceId : String?,
    val sourceName: String?,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val bookMarked: Boolean
)