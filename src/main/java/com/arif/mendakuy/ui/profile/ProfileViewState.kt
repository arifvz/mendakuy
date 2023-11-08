package com.arif.mendakuy.ui.profile

import com.arif.mendakuy.data.model.Post

sealed class ProfileViewState {

    data class Progress(val isLoading: Boolean) : ProfileViewState()
    data class FollowingPost(val postlist: List<Post>) : ProfileViewState()
    data class CreatedPost (val postlist: List<Post>) : ProfileViewState()
}
