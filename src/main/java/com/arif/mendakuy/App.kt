package com.arif.mendakuy

import android.app.Application
import android.util.Log
import com.arif.mendakuy.data.AuthManager
import com.arif.mendakuy.data.model.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        storage = Firebase.storage
    }

    companion object {

        private var mInstance: App? = null
        private var storage: FirebaseStorage? = null

        private fun getContext(): App {
            if (mInstance == null) {
                synchronized(App::class.java) {
                    mInstance = App()
                }
            }
            return mInstance!!
        }

        fun isLogin() = AuthManager.isLogin(getContext())

        fun getUser(): User? {
            return AuthManager.getUser(getContext())
        }

        fun getUserName(): String? {
            return getUser()?.username
        }

        fun getFullName(): String? {
            return getUser()?.fullName
        }

        fun String?.getAvatarUrl(url: (String) -> Unit) {
            val ref = storage?.reference?.child("avatar/${this}.jpg")
            Log.e("getAvatarUrl", "avatar/${this}")
            ref?.downloadUrl?.addOnSuccessListener {
                url.invoke(it.toString())
            }
        }

    }
}