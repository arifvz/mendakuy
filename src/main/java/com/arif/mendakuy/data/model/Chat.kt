package com.arif.mendakuy.data.model


data class Chat(
    var groupId: String? = null,
    val groupName: String? = null,
    val mount: Mount? = null,
    val participants: MutableList<String>? = null,
    val messages: MutableList<Messages>? = null,
)
