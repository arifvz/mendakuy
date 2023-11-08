package com.arif.mendakuy.ui.create

import com.arif.mendakuy.data.model.Post

sealed class CreateViewState {

    data class Progress(val isLoading: Boolean) : CreateViewState()
    data class CreateResult(
        val message: String? = null,
        val post: Post? = null
    ) : CreateViewState()
}
