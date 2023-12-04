package com.example.mypetgroup10

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationRequest



class WalkScreen : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var locationCallback: LocationCallback
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
        .apply {
            setWaitForAccurateLocation(false)
            setMaxUpdateDelayMillis(10000)
        }.build()


    private var mGoogleMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_screen)
        val navigateToPlayRoomButton: Button = findViewById(R.id.back_b_walk)
        navigateToPlayRoomButton.setOnClickListener {
            SetNewScreen(Screens.Home)

        }

        //Used to import devices last know location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {

        }

        //createLocationRequest()
        getCurrentLocationUser()
        startLocationUpdates()
        mainHandler.post(object : Runnable {
            override fun run() {
                startLocationUpdates()
                getCurrentLocationUser()
                mainHandler.postDelayed(this, 3000)
            }
        })
    }

    private fun SetNewScreen(screen: Screens) {
        // Implement the logic to switch to the selected screen based on the 'screen' parameter.
        Log.d("MyApp", "Screen " + screen)
        when (screen) {
            Screens.Home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            Screens.Shop -> {
                val intent = Intent(this, ShopScreen::class.java)
                startActivity(intent)
            }

            Screens.PlayRoom -> {
                val intent = Intent(this, PlayRoom::class.java)
                startActivity(intent)
            }

            Screens.Walk -> {
                val intent = Intent(this, WalkScreen::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getCurrentLocationUser() {
        //Check if permission for location is granted, request permission if it is not
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                permissionCode
            )
            return
        }
        val getLocation =
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location

                    val mapFragment =
                        supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
                    mapFragment.getMapAsync(this)
                }
            }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                getCurrentLocationUser()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("Sijainti")

        val toast = Toast.makeText(this, latLng.toString(), Toast.LENGTH_SHORT)
        toast.show()

        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        googleMap?.addMarker(markerOptions)
        mGoogleMap = googleMap
    }
    private fun energy() {
        val energyBar: ProgressBar = findViewById(R.id.energy_mainact_slider)
        var energyMax = 100
        var energyAmount = energyMax
        energyBar.max = energyMax
        energyBar.progress = energyAmount
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return

        }
        val toast = Toast.makeText(this, "testi", Toast.LENGTH_SHORT)
        toast.show()
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }


    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    fun createLocationRequest() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
            .apply {
                setWaitForAccurateLocation(false)
                setMaxUpdateDelayMillis(10000)
            }.build()
    }



}