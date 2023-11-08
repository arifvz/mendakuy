package com.arif.mendakuy.ui.profile

sealed class UpdateProfileViewState {

    data class Progress(val isLoading: Boolean) : UpdateProfileViewState()
    data class UpdatePicture(val message: String? = null) : UpdateProfileViewState()
    data class UpdateProfile(val message: String? =null) : UpdateProfileViewState()
    data class Success(val message: String) : UpdateProfileViewState()
    data class Error(val error: String) : UpdateProfileViewState()
}
