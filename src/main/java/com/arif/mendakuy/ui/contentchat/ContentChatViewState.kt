package com.arif.mendakuy.ui.contentchat

import com.arif.mendakuy.data.model.Messages

sealed class ContentChatViewState {

    data class ShowChat(val showChatContent: List<Messages>) : ContentChatViewState()

}