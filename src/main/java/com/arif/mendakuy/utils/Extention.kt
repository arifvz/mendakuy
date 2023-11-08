package com.arif.mendakuy.utils

import com.google.gson.Gson

fun Any.toJson(): String? {
    return Gson().toJson(this)
}
