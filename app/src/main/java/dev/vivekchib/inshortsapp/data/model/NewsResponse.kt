package dev.vivekchib.inshortsapp.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.vivekchib.inshortsapp.domain.entity.Article
import java.time.LocalDateTime
import java.time.ZoneOffset

data class NewsResponse(
    val articles: List<ArticleModel>,
    val status: String,
    val totalResults: Int
)

@Entity(tableName = "articles")
data class ArticleModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: SourceModel,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val timestamp: Long? = null,
    val bookmarked: Boolean? = null
) {

    fun toEntity() = Article(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        title = title,
        url = url,
        urlToImage = urlToImage,
        sourceId = source.id,
        sourceName = source.name,
        bookMarked = bookmarked ?: false
    )

    fun addTimestamp() = this.copy(
        timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    )

    companion object {
        fun fromEntity(article: Article): ArticleModel {
            return ArticleModel(
                author = article.author,
                content = article.content,
                description = article.description,
                publishedAt = article.publishedAt,
                title = article.title,
                url = article.url,
                urlToImage = article.urlToImage,
                source = SourceModel(
                    id = article.sourceId,
                    name = article.sourceName
                ),
            )
        }
    }
}

data class SourceModel(
    val id: String?,
    val name: String?
)