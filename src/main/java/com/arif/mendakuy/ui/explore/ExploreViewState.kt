package com.arif.mendakuy.ui.explore

import com.arif.mendakuy.data.model.Post
import com.google.ar.core.TrackingState

sealed class ExploreViewState {

    data class Progress(val isLoading: Boolean) : ExploreViewState()
    object Idle : ExploreViewState()
    object TrackingStarted : ExploreViewState()
    object TrackingStopped : ExploreViewState()
}