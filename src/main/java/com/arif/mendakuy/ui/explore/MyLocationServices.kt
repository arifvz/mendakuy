package com.arif.mendakuy.ui.explore

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData

class MyLocationServices() : Service() {

    private val binder: LocationServiceBinder = LocationServiceBinder()

    private var interval = 10000
    private var distance = 10F

    private var locationManager: LocationManager? = null

    private var locationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            Log.i("location", "${location.latitude} , ${location.longitude}}")
            lastLocation = location

            trackingRoute.add(location)

            subscribeLocation.value = location

        }

        override fun onStatusChanged(
            provider: String,
            status: Int,
            extras: Bundle
        ) {
        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onProviderDisabled(provider: String) {

        }
    }

    companion object {
        private var services: MyLocationServices? = null
        var lastLocation: Location? = null
        var trackingRoute : MutableList<Location> = arrayListOf()
        var stopRoute : MutableList<Location>? = null

        var subscribeLocation: MutableLiveData<Location> = MutableLiveData()

        fun getService(): MyLocationServices? {
            return services
        }

        fun turnOnGPS(context: Context) {
            AlertDialog.Builder(context)
                .setTitle("GPS Mati")
                .setMessage("Mohon nyalakan GPS Anda!")
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ ->
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }.show()
        }
    }

    class LocationServiceBinder : Binder() {
        val service: MyLocationServices
            get() = MyLocationServices()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }

    override fun onCreate() {
        services = this
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.FOREGROUND_SERVICE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startForeground(22122019, getNotification())
        }
    }

    private fun getNotification(): Notification {
        val notificationManager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_mcs_v2",
                "Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(channel)
        }

        val builder =
            NotificationCompat.Builder(applicationContext, "channel_mcs_v2").setAutoCancel(true)
        return builder.build()
    }

    fun startTracking() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        trackingRoute = arrayListOf()

        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            interval.toLong(), distance, locationListener
        )
    }


    fun stopTracking() {
        locationManager?.removeUpdates(locationListener)
    }

}