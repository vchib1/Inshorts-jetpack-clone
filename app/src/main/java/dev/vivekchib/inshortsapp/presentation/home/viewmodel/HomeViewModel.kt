package dev.vivekchib.inshortsapp.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vivekchib.inshortsapp.domain.entity.Article
import dev.vivekchib.inshortsapp.domain.usecases.news.AddBookmarkArticleUseCase
import dev.vivekchib.inshortsapp.domain.usecases.news.GetBookmarkArticlesUseCase
import dev.vivekchib.inshortsapp.domain.usecases.news.GetTopHeadlinesUseCase
import dev.vivekchib.inshortsapp.domain.usecases.news.RemoveBookmarkArticleUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlines: GetTopHeadlinesUseCase,
    private val addBookmarkUseCase: AddBookmarkArticleUseCase,
    private val removeBookmarkArticleUseCase: RemoveBookmarkArticleUseCase,
    private val getBookmarkArticlesUseCase: GetBookmarkArticlesUseCase,
) :
    ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        viewModelScope.async {
            val result: Result<List<Article>> = async { getTopHeadlines("us") }.await()

            val articles = result.getOrDefault(emptyList())

            updateState(articles = articles)

            getBookmarkArticlesUseCase(Unit).collect { bookmark ->
                val bookmarkedUrls = bookmark.map { it.url }

                val updatedArticles = _state.value.articles.map { article ->
                    val isBookmarked = bookmarkedUrls.contains(article.url)

                    if (article.bookMarked != isBookmarked) {
                        article.copy(bookMarked = isBookmarked)
                    } else {
                        article
                    }
                }

                updateState(articles = updatedArticles)
            }
        }
    }


    fun onEvent(event: HomeEvent) {

        Log.d(TAG, "onEvent: $event")
        when (event) {
            is HomeEvent.GetTopHeadlines -> getTopHeadlines()
            is HomeEvent.ToggleBookmark -> toggleBookMark(event.article)
            is HomeEvent.ShowDialogError -> showDialogError(event.message)
            is HomeEvent.ClearDialogError -> clearDialogError()
            is HomeEvent.ShowSnackBarError -> showSnackBarError(event.message)
            is HomeEvent.ClearSnackBarError -> clearSnackBarError()
        }
    }

    private fun updateState(
        isLoading: Boolean? = null,
        dialogError: String? = null,
        snackBarError: String? = null,
        articles: List<Article>? = null,
    ) {
        _state.value = _state.value.copy(
            isLoading = isLoading ?: state.value.isLoading,
            dialogError = dialogError ?: state.value.dialogError,
            snackBarError = snackBarError ?: state.value.snackBarError,
            articles = articles ?: state.value.articles
        )

        Log.d(TAG, "updateState: ${state.value}")
    }

    private fun getTopHeadlines() {
        viewModelScope.launch {
            updateState(isLoading = true)

            val response = getTopHeadlines("us")

            response.onSuccess { articles ->
                updateState(articles = articles, isLoading = false)
            }.onFailure { error ->
                updateState(
                    dialogError = error.message ?: "Something went wrong",
                    isLoading = false
                )
            }
        }
    }


    private fun toggleBookMark(article: Article) {
        viewModelScope.launch {
            val result = if (!article.bookMarked) {
                addBookmarkUseCase(article)
            } else {
                removeBookmarkArticleUseCase(article)
            }

            result.onSuccess {
                val message =
                    if (!article.bookMarked) "Added to Bookmark" else "Removed from Bookmark"

                Log.d("HomeViewModel", message)

                val updatedArticles = state.value.articles.map {
                    if (it.url == article.url) {
                        it.copy(bookMarked = !article.bookMarked)
                    } else {
                        it
                    }
                }

                updateState(articles = updatedArticles, snackBarError = message)
            }.onFailure {
                it.message?.let { msg ->
                    showSnackBarError(msg)
                }
            }


        }
    }

    private fun showDialogError(message: String) = updateState(dialogError = message)
    private fun clearDialogError() = updateState(dialogError = "")
    private fun showSnackBarError(message: String) = updateState(snackBarError = message)
    private fun clearSnackBarError() = updateState(snackBarError = "")
}


