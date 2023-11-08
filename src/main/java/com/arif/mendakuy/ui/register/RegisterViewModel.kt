package com.arif.mendakuy.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.mendakuy.data.K
import com.arif.mendakuy.data.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {

    private val _state = MutableLiveData<RegisterViewState>()
    val state: LiveData<RegisterViewState> = _state

    val db = Firebase.firestore

    fun register(user: User) {
        val username = user.username ?: return
        _state.value = RegisterViewState.Progress(true)
        db.collection("users")
            .document(username)
            .get()
            .addOnSuccessListener {
                _state.value = RegisterViewState.Progress(false)
                if (it.toObject(User::class.java) == null) {
                    doRegister(user)
                } else {
                    _state.value = RegisterViewState.RegisterResult(message = K.USER_EXIST)
                }
            }
            .addOnFailureListener {
                _state.value = RegisterViewState.Progress(false)
                _state.value = RegisterViewState.RegisterResult(message = K.REGISTER_FAIL)
            }

    }

    private fun doRegister(user: User) {
        val username = user.username ?: return
        db.collection("users")
            .document(username)
            .set(user)
            .addOnSuccessListener {
                _state.value = RegisterViewState.Progress(false)
                _state.value = RegisterViewState.RegisterResult(
                    userId = username,
                    message = K.REGISTER_SUCCESS
                )
            }
            .addOnFailureListener { exception ->
                _state.value = RegisterViewState.Progress(false)
                _state.value = RegisterViewState.RegisterResult(message = K.REGISTER_FAIL)
            }

    }
}