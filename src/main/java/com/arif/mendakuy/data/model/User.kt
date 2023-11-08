package com.arif.mendakuy.data.model

import java.util.*

data class User(
    var id: String? = null,
    val avatarUrl:String? = null,
    val fullName: String? = null,
    val phoneNumber: String? = null,
    val username: String? = null,
    val password: String? = null,
    val createdAt: Date? = null
)
