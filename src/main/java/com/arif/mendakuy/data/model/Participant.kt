package com.arif.mendakuy.data.model

import java.util.*

data class Participant(
    val user: User? = null,
    var status: String? = null,
    val message: String? = null,
    val requestDate: Date? = null
)