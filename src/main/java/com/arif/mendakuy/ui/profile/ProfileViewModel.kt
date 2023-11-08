package com.arif.mendakuy.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.mendakuy.App
import com.arif.mendakuy.data.model.Post
import com.arif.mendakuy.data.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user : LiveData<User?> = _user

    private val _state = MutableLiveData<ProfileViewState>()
    val state: LiveData<ProfileViewState> = _state

    val database = Firebase.firestore
    val storage = Firebase.storage

    fun getProfile(){
        val user = App.getUser()
        _user.value = user
        database.collection("users")
            .document(user?.username.toString())
            .get()
            .addOnSuccessListener { result ->
                val userResult = result.toObject(User::class.java)
                _user.value = userResult
            }
            .addOnFailureListener { exception ->
            }
    }

    fun getFollowingPost() {
        val username = App.getUser()?.username ?: return
        database.collection("post")
            .whereArrayContains("participantUsername", username)
            .get()
            .addOnSuccessListener { result ->
                val list = arrayListOf<Post>()
                for (post in result.documents) {
                    post.toObject(Post::class.java)?.let {
                        it.id = post.id
                        list.add(it)
                    }
                }
                _state.value = ProfileViewState.FollowingPost(list)
            }
            .addOnFailureListener {
            }
    }

    fun getCreatedPost() {
        val username = App.getUser()?.username ?: return

        database.collection("post")
            .whereEqualTo("user.username", username)
            .get()
            .addOnSuccessListener { result ->
                val list = arrayListOf<Post>()
                for (post in result.documents) {
                    post.toObject(Post::class.java)?.let {
                        it.id = post.id
                        list.add(it)
                    }
                }
                _state.value = ProfileViewState.CreatedPost(list)
            }
            .addOnFailureListener {
            }
    }

}


