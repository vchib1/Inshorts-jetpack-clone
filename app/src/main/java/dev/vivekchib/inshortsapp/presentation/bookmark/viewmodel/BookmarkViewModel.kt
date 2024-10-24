package dev.vivekchib.inshortsapp.presentation.bookmark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vivekchib.inshortsapp.domain.entity.Article
import dev.vivekchib.inshortsapp.domain.usecases.news.GetBookmarkArticlesUseCase
import dev.vivekchib.inshortsapp.domain.usecases.news.RemoveBookmarkArticleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getBookmarkArticlesUseCase: GetBookmarkArticlesUseCase,
    private val removeBookmarkArticleUseCase: RemoveBookmarkArticleUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<List<Article>>(emptyList())
    val state: StateFlow<List<Article>> = _state

    init {
        viewModelScope.launch {
            getBookmarkArticlesUseCase(Unit).collect {
                _state.value = it
            }
        }
    }

    fun removeBookmark(article : Article){
        viewModelScope.launch{
            removeBookmarkArticleUseCase(article)
        }
    }

}