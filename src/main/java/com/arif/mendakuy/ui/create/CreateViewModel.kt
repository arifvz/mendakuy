package com.arif.mendakuy.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.mendakuy.App
import com.arif.mendakuy.data.K
import com.arif.mendakuy.data.model.Chat
import com.arif.mendakuy.data.model.Mount
import com.arif.mendakuy.data.model.Post
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateViewModel : ViewModel() {

    private val _mountList = MutableLiveData<List<Mount>>()
    val mountList: LiveData<List<Mount>> = _mountList

    private val database = Firebase.firestore

    val detailPost = MutableLiveData<String>()

    private val _state = MutableLiveData<CreateViewState>()
    val state: LiveData<CreateViewState> = _state

    fun getMount() {
        database.collection("mountlist")
            .get()
            .addOnSuccessListener { result ->
                val list = arrayListOf<Mount>()
                for (mount in result.documents) {
                    mount.toObject(Mount::class.java)?.let {
                        list.add(it)
                    }
                }
                _mountList.value = list
            }
            .addOnFailureListener { _ ->
            }
    }

    fun onCreate(post: Post) {
        _state.value = CreateViewState.Progress(true)
        database.collection("post")
            .add(post)
            .addOnSuccessListener { documentReference ->
                _state.value = CreateViewState.Progress(false)
                detailPost.value = documentReference.id
                if (documentReference != null) {
                    _state.value = CreateViewState.CreateResult(
                        post = post, message = K.CREATE_SUCCESS
                    )
                    createChatRoom(documentReference.id, post)
                } else {
                    _state.value =
                        CreateViewState.CreateResult(message = K.CREATE_FAIL)
                }
            }
            .addOnFailureListener {
                _state.value = CreateViewState.Progress(false)
                _state.value = CreateViewState.CreateResult(message = K.CREATE_FAIL)
            }
    }

    private fun createChatRoom(groupId: String, post : Post?) {
        val chat = Chat(
            groupId = groupId,
            mount = post?.mount,
            groupName = post?.postTitle,
            participants = arrayListOf(App.getUserName() ?: ""),
        )
        database.collection("chats")
            .add(chat)
            .addOnSuccessListener {

            }
    }
}