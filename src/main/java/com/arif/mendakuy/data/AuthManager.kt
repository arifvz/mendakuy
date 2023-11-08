package com.arif.mendakuy.data

import android.content.Context
import com.arif.mendakuy.data.model.User
import com.arif.mendakuy.utils.toJson
import com.google.gson.Gson

object AuthManager {

    fun getUser(context: Context): User? {
        val sharedPreferences = context.getSharedPreferences("user_login", Context.MODE_PRIVATE)
        val strUser = sharedPreferences.getString("user", null) ?:return null
        return Gson().fromJson(strUser, User::class.java)
    }

    fun isLogin(context: Context): Boolean {
        return getUser(context) != null
    }

    fun setUser(context: Context, user: User?) {
        if (user != null) {
            val sharedPreferences = context.getSharedPreferences("user_login", Context.MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putString("user", user.toJson())
                apply()
            }
        }
    }

    fun logOut(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_login", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("user")
            commit()
        }
    }
}
