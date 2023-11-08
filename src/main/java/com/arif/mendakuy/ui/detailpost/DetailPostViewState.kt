package com.arif.mendakuy.ui.detailpost

sealed class DetailPostViewState {

    data class Progress(val isLoading: Boolean) : DetailPostViewState()
    data class DeleteResult(val message: String? = null) : DetailPostViewState()
}