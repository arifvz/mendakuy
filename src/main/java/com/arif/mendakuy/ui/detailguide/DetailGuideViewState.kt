package com.arif.mendakuy.ui.detailguide

sealed class DetailGuideViewState {

    data class Progress(val isLoading: Boolean) : DetailGuideViewState()
}