package com.arif.mendakuy.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.mendakuy.App
import com.arif.mendakuy.data.model.Chat
import com.arif.mendakuy.data.model.Messages
import com.arif.mendakuy.ui.contentchat.ContentChatViewState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class ContentChatViewModel : ViewModel() {


    private val database = Firebase.firestore

    private val _chatContent = MutableLiveData<Chat>()
    val chatContent: LiveData<Chat> = _chatContent

    private val _state = MutableLiveData<ContentChatViewState>()
    val state: LiveData<ContentChatViewState> = _state

    var groupId: String? = null
    var groupChat: Chat? = null

    fun getChat() {
        val groupId = groupId ?: return
        database.collection("chats")
            .document(groupId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("error", "Failed : $error");
                    return@addSnapshotListener
                }
                if (value != null && value.exists()) {
                    Log.e("value", "$value")
                    groupChat = value.toObject(Chat::class.java)
                    showMessage(groupChat?.messages)
                }
            }
    }

    private fun showMessage(messages: MutableList<Messages>?) {
        _state.value = ContentChatViewState.ShowChat(messages?.toList() ?: arrayListOf())
    }

    fun sendMessage(message: String) {
        val groupId = groupId ?: return
        val listMessages = groupChat?.messages ?: arrayListOf()
        val user = App.getUser()
        listMessages.add(
            Messages(
                message = message,
                fullName = user?.fullName,
                username = user?.username,
                avatarUrl = user?.avatarUrl,
                dateTime = Date()
            )
        )

        database.collection("chats")
            .document(groupId)
            .update("messages", listMessages)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

}

