package dev.vivekchib.inshortsapp.presentation.home.viewmodel

import dev.vivekchib.inshortsapp.domain.entity.Article

sealed class HomeEvent {
    data object GetTopHeadlines : HomeEvent()

    data class ShowDialogError(val message: String) : HomeEvent()
    data object ClearDialogError : HomeEvent()

    data class ShowSnackBarError(val message: String) : HomeEvent()
    data object ClearSnackBarError : HomeEvent()

    data class ToggleBookmark(val article: Article) : HomeEvent()
}


