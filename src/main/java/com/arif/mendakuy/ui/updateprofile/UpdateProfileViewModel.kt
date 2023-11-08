package com.arif.mendakuy.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.mendakuy.App
import com.arif.mendakuy.data.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UpdateProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user : LiveData<User?> = _user

    private val _state = MutableLiveData<UpdateProfileViewState>()
    val state: LiveData<UpdateProfileViewState> = _state

    private val database = Firebase.firestore
    private val storage = Firebase.storage

    fun getProfile(){
        val user = App.getUser()
        _user.value = user
        database.collection("users")
            .whereEqualTo("username", user?.username)
            .get()
            .addOnSuccessListener { result ->
                val userResult = result.toObjects(User::class.java)
                _user.value = userResult.firstOrNull()
            }
            .addOnFailureListener {
            }
    }

    fun onUpdateProfile(user: User) {
        database.collection("users")
            .document(App.getUser()?.username.toString())
            .update(
                "fullName", user.fullName,
                "password", user.password,
                "phoneNumber", user.phoneNumber
            ) .addOnSuccessListener {
                _state.value = UpdateProfileViewState.UpdateProfile()
            }
            .addOnFailureListener { e ->
                _state.value = UpdateProfileViewState.Progress(false)
            }
    }

    fun uploadProfilePict(stream: ByteArray) {
        Log.e("uploadProfilePict", "Start")
        val storageRef = storage.reference.child("avatar/${App.getUserName()}.jpg")
        _state.value = UpdateProfileViewState.Progress(true)
        val uploadTask = storageRef.putBytes(stream)
        uploadTask.addOnSuccessListener {
            Log.e("uploadProfilePict", it.toString())
            _state.value = UpdateProfileViewState.Progress(false)
            _state.value = UpdateProfileViewState.UpdatePicture(message = "Berhasil Mengubah Foto Profile")
        }.addOnFailureListener{
            it.printStackTrace()
            _state.value = UpdateProfileViewState.Progress(false)
            _state.value = UpdateProfileViewState.UpdatePicture(message = "Gagal Mengubah Foto Profile")
        }
    }
}

