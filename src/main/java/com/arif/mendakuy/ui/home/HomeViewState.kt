package com.arif.mendakuy.ui.home

import com.arif.mendakuy.data.model.Post

sealed class HomeViewState {

    data class Progress(val isLoading: Boolean) : HomeViewState()
    data class ShowPost(val postlist: List<Post>) : HomeViewState()
    data class JoinResult(
        val message: String? = null,
        val post: Post? = null
    ) : HomeViewState()
}