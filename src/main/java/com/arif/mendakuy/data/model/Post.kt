package com.arif.mendakuy.data.model

import java.util.*

data class Post(
    var id: String? = null,
    val user: User? = null,
    val mount: Mount? = null,
    val postTitle: String? = null,
    val postDesc: String? = null,
    val mountTrails: String? = null,
    val meetPoint: String? = null,
    val dateStart: Date? = null,
    val dateFinish: Date? = null,
    val participants: MutableList<Participant>? = null,
    val participantsUsername: MutableList<String>? = null,
)




