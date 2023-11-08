package com.arif.mendakuy.ui.register

sealed class RegisterViewState {

    data class Progress(val isLoading: Boolean) : RegisterViewState()
    data class RegisterResult(
        val message: String? = null,
        val userId: String? = null
    ) : RegisterViewState()
}