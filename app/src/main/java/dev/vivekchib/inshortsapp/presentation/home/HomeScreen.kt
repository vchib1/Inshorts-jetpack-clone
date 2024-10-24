package dev.vivekchib.inshortsapp.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.google.gson.Gson
import dev.vivekchib.inshortsapp.R
import dev.vivekchib.inshortsapp.core.extension.formattedDateTime
import dev.vivekchib.inshortsapp.core.extension.parseISO8601
import dev.vivekchib.inshortsapp.core.theme.InshortsappTheme
import dev.vivekchib.inshortsapp.data.model.ArticleModel
import dev.vivekchib.inshortsapp.presentation.home.viewmodel.HomeEvent
import dev.vivekchib.inshortsapp.presentation.home.viewmodel.HomeState

@Preview
@Composable
private fun HomeScreenPreview() {
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

    InshortsappTheme(true) {
        HomeScreen(
            state = HomeState(
                isLoading = false,
                dialogError = "",
                snackBarError = "",
                articles = listOf(article)
            ),
            onEvent = {},
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    openUrl: (String) -> Unit = {}
) {

    val height = LocalConfiguration.current.screenHeightDp

    val snackBarHostState = remember { SnackbarHostState() }

    val articles = state.articles

    val pagerState = rememberPagerState(pageCount = { state.articles.size })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
    ) { it ->

        // SnackBar
        LaunchedEffect(state.snackBarError) {
            if (state.snackBarError.isNotEmpty()) {
                snackBarHostState.showSnackbar(message = state.snackBarError)
                //delay(100L)
                onEvent(HomeEvent.ClearSnackBarError)
            }
        }


        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.TopCenter
            ) {
                CircularProgressIndicator()
            }
        }

        if (state.dialogError.isNotEmpty()) {
            ErrorDialog(
                error = state.dialogError,
                onDismissRequest = {
                    onEvent(HomeEvent.ClearDialogError)
                },
            )
        }

        VerticalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
        ) { index ->
            val article = articles[index]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable() { article.url?.let { openUrl(it) } }) {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height.dp * 0.4f),
                        model = article.urlToImage,
                        contentDescription = article.description,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_image_24),
                    )

                    val iconId =
                        if (article.bookMarked) R.drawable.ic_bookmark_enabled else R.drawable.ic_bookmark_disabled

                    Icon(
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { onEvent(HomeEvent.ToggleBookmark(article)) },
                        painter = painterResource(iconId),
                        contentDescription = "bookmark",
                    )
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = article.title ?: "NA",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    article.content?.let {
                        val content = article.content.split("…").first() + "..."
                        Text(
                            text = content,
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }


                    article.publishedAt?.let { iso ->
                        val formattedDateTime = parseISO8601(iso).formattedDateTime()

                        Text(
                            text = "Published: $formattedDateTime",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorDialog(error: String, onDismissRequest: () -> Unit) {
    BasicAlertDialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Error", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = error)
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("OK")
                }
            }
        }

    }
}