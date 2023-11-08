package com.arif.mendakuy.ui.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.mendakuy.data.model.Guide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GuideViewModel : ViewModel() {

    private val _guide = MutableLiveData<List<Guide>>()
    val guide : LiveData<List<Guide>> = _guide

    private val database = Firebase.firestore

    fun getGuideList() {
        database.collection("guide")
            .get()
            .addOnSuccessListener{ result ->
                val list = arrayListOf<Guide>()
                for (guide in result.documents) {
                    guide.toObject(Guide::class.java)?.let {
                        it.id = guide.id
                        list.add(it)
                    }
                }
                _guide.value = list
            }
            .addOnFailureListener {
            }
    }

}