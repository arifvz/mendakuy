package com.arif.mendakuy.ui.login

import com.arif.mendakuy.data.model.User


sealed class LoginViewState {

    data class Progress(val isLoading: Boolean) : LoginViewState()
    data class LoginResult(
        val message: String? = null,
        val user: User? = null
    ) : LoginViewState()

}