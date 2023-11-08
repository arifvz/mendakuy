package com.arif.mendakuy.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.mendakuy.data.K
import com.arif.mendakuy.data.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginViewModel : ViewModel() {

    val db = Firebase.firestore

    private val _state = MutableLiveData<LoginViewState>()
    val state: LiveData<LoginViewState> = _state

    fun onLogin(username: String, password: String) {
        _state.value = LoginViewState.Progress(true)
        db.collection("users")
            .whereEqualTo("username", username)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { result ->
                _state.value = LoginViewState.Progress(false)
                val document = result.documents.firstOrNull()
                val user = document?.toObject(User::class.java)
                if (user != null) {
                    user.id = document.id
                    _state.value = LoginViewState.LoginResult(
                        user = user,
                        message = K.LOGIN_SUCCESS)
                } else {
                    _state.value = LoginViewState.LoginResult(
                        message =
                    K.LOGIN_FAIL)
                }
            }.addOnFailureListener { exception ->
                _state.value = LoginViewState.Progress(false)
                _state.value = LoginViewState.LoginResult(
                    message =
                K.LOGIN_FAIL)

            }
    }
}

