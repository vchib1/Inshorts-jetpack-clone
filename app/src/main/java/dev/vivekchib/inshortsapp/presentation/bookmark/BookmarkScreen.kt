@file:OptIn(ExperimentalMaterial3Api::class)

package dev.vivekchib.inshortsapp.presentation.bookmark

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.gson.Gson
import dev.vivekchib.inshortsapp.R
import dev.vivekchib.inshortsapp.data.model.ArticleModel
import dev.vivekchib.inshortsapp.domain.entity.Article

@Preview
@Composable
fun PreviewBookmarkScreen() {

    val jsonResponse = """
    {
    "source": {
        "id": null,
        "name": "The Official Website of the Ultimate Fighting Championship"
    },
    "author": null,
    "title": "Official Scorecards | UFC 307: Pereira vs Rountree Jr. - UFC",
    "description": "See How The Judges Scored Every Round Of UFC 307: Pereira vs Rountree Jr., Live From Delta Center In Salt Lake City, Utah",
    "url": "https://www.ufc.com/news/official-judges-scorecards-ufc-307-pereira-vs-rountree-jr",
    "urlToImage": "https://dmxg5wxfqgb4u.cloudfront.net/styles/card/s3/2024-10/091424-Bruce-Buffer-Announcement-UFC-306-HERO-GettyImages-2172045908.jpg?itok=qyY4Gr0u",
    "publishedAt": "2024-10-06T07:27:21Z",
    "content": "It's always an incredible night when the Octagon touches down in Salt Lake City, Utah. UFC 307 was no different and a championship double header sat atop the marquee, with Alex Pereira and Julianna P… [+377 chars]"
    }
    """.trimIndent()

    val article = Gson().fromJson(jsonResponse, ArticleModel::class.java).toEntity()

    BookmarkScreen(articles = listOf(article))
}

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookmarkScreen(
    articles: List<Article>, onDelete: (Article) -> Unit = {}, openUrl: (String) -> Unit = {},
) {
    Scaffold(Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = { Text("Bookmarks") })
    }) {
        LazyColumn(Modifier.padding(it)) {
            items(articles, key = { it.url ?: "uniqueKey" }) { article ->
                val dismissState = rememberSwipeToDismissBoxState()

                OutlinedCard(
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { article.url?.let { openUrl(it) } }
                )
                {
                    SwipeToDismissBox(
                        modifier = Modifier.wrapContentSize(),
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = {

                            if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart &&
                                dismissState.currentValue != SwipeToDismissBoxValue.Settled
                            ) {
                                onDelete(article)
                            }


                            val color = animateColorAsState(
                                when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.onError
                                    SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.onError
                                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.onError
                                }, label = "BG COLOR"
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color.value, shape = MaterialTheme.shapes.large),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                IconButton(
                                    onClick = { onDelete(article) },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                }
                            }
                        },
                    ) {
                        ListItem(
                            leadingContent = {
                                AsyncImage(
                                    modifier = Modifier.size(75.dp),
                                    contentScale = ContentScale.FillBounds,
                                    model = article.urlToImage,
                                    placeholder = painterResource(id = R.drawable.ic_image_24),
                                    contentDescription = null
                                )
                            },
                            headlineContent = { Text(text = article.title ?: "NA", maxLines = 2) },
                            supportingContent = {
                                Text(text = article.description ?: "NA", maxLines = 2)
                            },
                        )
                    }
                }
            }
        }

    }
}