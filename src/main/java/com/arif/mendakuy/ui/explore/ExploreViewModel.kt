package com.arif.mendakuy.ui.explore

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExploreViewModel : ViewModel() {

    private val database = Firebase.firestore

//    private val fusedLocationClient: FusedLocationProviderClient =
//        LocationServices.getFusedLocationProviderClient(context)

    private val trackingLocations = mutableListOf<Location>()

    private var trackingStarted = false

    fun startTracking() {

    }

    fun stopTracking() {
        trackingStarted = false
        // Stop location updates here
    }

    fun isTracking(): Boolean {
        return trackingStarted
    }

    fun getTrackingLocations(): List<Location> {
        return trackingLocations
    }
}