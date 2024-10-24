package dev.vivekchib.inshortsapp.presentation.home.viewmodel

import dev.vivekchib.inshortsapp.domain.entity.Article

data class HomeState(
    val isLoading: Boolean = false,
    val dialogError: String = "",
    val snackBarError: String = "",
    val articles: List<Article> = emptyList()
) {

    override fun toString(): String {
        return "HomeState: \nisLoading=$isLoading,\n dialogError=$dialogError,\n snackBarError=$snackBarError,\n articles=${articles.size}"
    }

}
