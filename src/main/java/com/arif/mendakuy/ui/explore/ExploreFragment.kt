package com.arif.mendakuy.ui.explore

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arif.mendakuy.databinding.FragmentExploreBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import java.util.*

class ExploreFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var exploreViewModel: ExploreViewModel
    private var _binding: FragmentExploreBinding? = null

    private val binding get() = _binding!!

    val myLocationServices = MyLocationServices
    private var mMap: GoogleMap? = null
    var geocode: Address? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback
    private var mLocationRequest: LocationRequest? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    val REQUEST_TURN_ON_GPS = 11

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exploreViewModel = ViewModelProvider(this)[ExploreViewModel::class.java]

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.btnStart.setOnClickListener {
            startTrackingLocation()
        }
        binding.btnStop.setOnClickListener {
            stopTrackingLocation()
        }

        binding.btnSendSMS.setOnClickListener {
            val address = binding.tvLocation.text
            val message = "My Last Location: ${address}" // Ganti trackingData dengan data tracking yang sesuai

            val smsUri = Uri.parse("sms:")
            val intent = Intent(Intent.ACTION_VIEW, smsUri)
            intent.putExtra("sms_body", message)
            startActivity(intent)
        }

        binding.mapView.getMapAsync(this)
        createLocationCallback()
        startTrackingLocation()
    }

    private fun createLocationService() {
        val intent = Intent(context, MyLocationServices::class.java)
        context?.startService(intent)
        context?.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        MyLocationServices.subscribeLocation.observe(this) {
            stylePolyline(MyLocationServices.trackingRoute)
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (name?.className == MyLocationServices::class.java.name) {
                MyLocationServices.getService()?.startTracking()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            if (name?.className == MyLocationServices::class.java.name) {
                MyLocationServices.getService()?.stopTracking()
            }
        }

    }

    private fun startTrackingLocation() = mLocationRequest?.let { locationRequest ->

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            .setAlwaysShow(true)
        val client: SettingsClient = LocationServices.getSettingsClient(context!!)
        client.checkLocationSettings(builder.build()).addOnSuccessListener {
            getCurrentLocation()
            createLocationService()
        }.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(context as Activity, REQUEST_TURN_ON_GPS)
                } catch (e: IntentSender.SendIntentException) {
                    // Ignore the error.
                } catch (e: ClassCastException) {
                    // Ignore, should be an impossible error.
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }


    private fun createLocationCallback() {
        val context = context ?: return
        mGoogleApiClient =
            GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build().apply {
                    connect()
                }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)

        mLocationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, (10 * 1000).toLong())
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis((1 * 1000).toLong())
                .setMaxUpdateDelayMillis((10 * 1000).toLong())
                .build()


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.firstOrNull()?.let { location: Location? ->
                    mLastLocation = location

                    Log.e("LocationCallback", "-> $location")

                    location?.apply {
                        mMap?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(latitude, longitude),
                                20f
                            )
                        )
                    }

                }
            }
        }
    }

    @SuppressLint("Range")
    private fun getCurrentLocation() {
        val context = context ?: return
        mLocationRequest?.let {
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestLocationPermission()
            } else {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)

                        mMap?.apply {
                            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                            addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_up_float))
                            )
                        }

                        geocode = Geocoder(context).getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )?.firstOrNull()

                        val address =
                            "${geocode?.getAddressLine(0)} (${geocode?.latitude},${geocode?.longitude}})"
                        binding.tvLocation.text = address

                    }
                    Log.e("location", location.toString())

                }

                fusedLocationProviderClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                            return CancellationTokenSource().token
                        }

                        override fun isCancellationRequested(): Boolean {
                            return false
                        }
                    }).addOnSuccessListener { location ->
                    if (location != null) {
                        mMap?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(location.latitude, location.longitude), 15f
                            )
                        )
                    }
                }

            }
        }

    }

    override fun onPause() {
        super.onPause()
        stopTrackingLocation()
    }

    private fun stopTrackingLocation() {
        mMap?.clear()
    }

    @SuppressLint("MissingPermission")
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = true
            }
            else -> {
            }
        }
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun onMapReady(gogleMap: GoogleMap) {
        mMap = gogleMap

        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.684, 133.903), 4f))
    }

    private val POLYLINE_STROKE_WIDTH_PX = 16
    private val COLOR_BLACK_ARGB = -0xa80e9

    private fun stylePolyline(routeList: List<Location>) {

        val options = PolylineOptions()
        routeList.forEach { location ->
            options.add(LatLng(location.latitude, location.longitude))
        }

        val polyline1 = mMap?.addPolyline(options)

        val type = polyline1?.tag?.toString() ?: ""
        when (type) {
            "A" -> {
                polyline1?.startCap = CustomCap(
                    BitmapDescriptorFactory.fromResource(R.drawable.arrow_up_float), 10f
                )
            }
            "B" -> {
                polyline1?.startCap = RoundCap()
            }
        }

        polyline1?.endCap = RoundCap()
        polyline1?.width = POLYLINE_STROKE_WIDTH_PX.toFloat()
        polyline1?.color = COLOR_BLACK_ARGB
        polyline1?.jointType = JointType.ROUND
    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


}

