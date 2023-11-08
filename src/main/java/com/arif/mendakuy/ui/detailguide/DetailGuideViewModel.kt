package com.arif.mendakuy.ui.detailguide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.mendakuy.data.model.Guide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailGuideViewModel : ViewModel() {

    private val database = Firebase.firestore

    private val _detailGuide = MutableLiveData<Guide>()
    val detailGuide: LiveData<Guide> = _detailGuide

    private val _state = MutableLiveData<DetailGuideViewState>()
    val state: LiveData<DetailGuideViewState> = _state

    fun getDetailGuide(id: String?) {
        if (id == null) return
        database.collection("guide")
            .document(id)
            .addSnapshotListener { result, error ->
                if (result == null || error != null) {
                    return@addSnapshotListener
                }
                val detail: Guide? = result.toObject(Guide::class.java)
                detail?.id = result.id
                detail?.let {
                    _detailGuide.value = it
                }
            }
    }

}