package com.arif.mendakuy.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.mendakuy.App
import com.arif.mendakuy.data.model.Chat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ChatViewModel : ViewModel() {


    private val database = Firebase.firestore

    private val _state = MutableLiveData<ChatViewState>()
    val state: LiveData<ChatViewState> = _state

    fun getListChat() {
        database.collection("chats")
            .whereArrayContains("participants", App.getUserName() ?: "")
            .get()
            .addOnSuccessListener { result ->
                val list = arrayListOf<Chat>()
                for (chat in result.documents) {
                    chat.toObject(Chat::class.java)?.let {
                        it.groupId = chat.id
                        list.add(it)
                    }
                }
                _state.value = ChatViewState.ShowListChat(list)
            }
            .addOnFailureListener {
            }
    }
}

