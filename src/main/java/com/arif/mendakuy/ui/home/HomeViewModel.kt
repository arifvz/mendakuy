package com.arif.mendakuy.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.mendakuy.App
import com.arif.mendakuy.data.K
import com.arif.mendakuy.data.model.Participant
import com.arif.mendakuy.data.model.Post
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class HomeViewModel : ViewModel() {

    val db = Firebase.firestore

    private val _state = MutableLiveData<HomeViewState>()
    val state: LiveData<HomeViewState> = _state

    fun getPost() {
        _state.value = HomeViewState.Progress(true)
        db.collection("post")
            .get()
            .addOnSuccessListener { result ->
                _state.value = HomeViewState.Progress(false)
                val list = arrayListOf<Post>()
                for (post in result.documents) {
                    post.toObject(Post::class.java)?.let {
                        it.id = post.id
                        list.add(it)
                    }
                }
                _state.value = HomeViewState.ShowPost(list)
            }
            .addOnFailureListener {
                _state.value = HomeViewState.Progress(false)
            }
    }

    fun onJoin(post: Post?, message: String) {
        val postId = post?.id ?: return
        val user = App.getUser() ?: return
        _state.value = HomeViewState.Progress(true)

        val request = Participant(
            user = user,
            message = message,
            status = K.STATUS_REQUEST,
            requestDate = Date()
        )
        val participants = post.participants ?: arrayListOf()
        participants.add(request)
        val participantUsername = post.participantsUsername ?: arrayListOf()
        user.username?.let { participantUsername.add(it) }

        val map = HashMap<String, Any>()
        map["participants"] = participants
        map["participantUsername"] = participantUsername

        db.collection("post")
            .document(postId)
            .update(map)
            .addOnSuccessListener {
                _state.value = HomeViewState.Progress(false)
                _state.value = HomeViewState.JoinResult(message = K.REQUEST_SUCCESS)
            }
            .addOnFailureListener {
                _state.value = HomeViewState.Progress(false)
                _state.value = HomeViewState.JoinResult(message = K.REQUEST_FAIL)
            }

    }
}