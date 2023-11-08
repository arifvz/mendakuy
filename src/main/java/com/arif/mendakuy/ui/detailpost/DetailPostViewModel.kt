package com.arif.mendakuy.ui.detailpost

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.mendakuy.App
import com.arif.mendakuy.data.model.Chat
import com.arif.mendakuy.data.model.Participant
import com.arif.mendakuy.data.model.Post
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailPostViewModel : ViewModel() {

    val database = Firebase.firestore

    private val _detailPost = MutableLiveData<Post>()
    val detailPost: LiveData<Post> = _detailPost

    private val _state = MutableLiveData<DetailPostViewState>()
    val state: LiveData<DetailPostViewState> = _state

    fun getDetailPost(id: String?) {
        if (id == null) return
        database.collection("post")
            .document(id)
            .addSnapshotListener { result, error ->
                if (result == null || error != null) {
                    return@addSnapshotListener
                }
                val detail: Post? = result.toObject(Post::class.java)
                detail?.id = result.id
                detail?.let {
                    _detailPost.value = it
                }
            }
    }

    fun onUpdateStatus(participant: Participant, status: String) {
        val postId = _detailPost.value?.id ?: return
        if (!App.isLogin()) {
            return
        }

        _state.value = DetailPostViewState.Progress(true)
        val participants = _detailPost.value?.participants ?: arrayListOf()
        participants.firstOrNull { it.user?.username == participant.user?.username }?.status =
            status

        database.collection("post")
            .document(postId)
            .update("participants", participants)
            .addOnSuccessListener {
                Log.e("onUpdateStatus", "$status")

                if (status == "ACCEPTED") {
                    addToGroupChat(postId, participant)
                }
                _state.value = DetailPostViewState.Progress(false)
            }
            .addOnFailureListener { _ ->
                _state.value = DetailPostViewState.Progress(false)
            }
    }

    private fun addToGroupChat(postId: String, participant: Participant) {
        Log.e("postId", "$postId")
        database.collection("chats")
            .whereEqualTo("groupId", postId)
            .get()
            .addOnSuccessListener { documents ->
                Log.e("addToGroupChat", "$documents")
                val document = documents?.firstOrNull()
                document?.toObject(Chat::class.java)?.let { chat ->
                    updateParticipants(document.id, chat, participant.user?.username)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun updateParticipants(documentId: String, chat: Chat, username: String?) {
        database.collection("chats")
            .document(documentId)
            .update("participants", FieldValue.arrayUnion(username))
            .addOnSuccessListener {
                Log.e("chat", "success add participant")
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun deletePost(post: Post) {
        val postId = post.id
        if (postId != null) {
            database.collection("post")
                .document(postId)
                .delete()
                .addOnSuccessListener {
                    _state.value =
                        DetailPostViewState.DeleteResult(message = "Kegiatan Berhasil Dihapus")
                }
                .addOnFailureListener {
                    _state.value =
                        DetailPostViewState.DeleteResult(message = "Kegiatan Gagal Dihapus")
                }
        }
    }

}