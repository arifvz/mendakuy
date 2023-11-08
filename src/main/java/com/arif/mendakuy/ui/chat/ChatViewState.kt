package com.arif.mendakuy.ui.chat

import com.arif.mendakuy.data.model.Chat

sealed class ChatViewState {

    data class ShowListChat(val listchat: List<Chat>) : ChatViewState()

}