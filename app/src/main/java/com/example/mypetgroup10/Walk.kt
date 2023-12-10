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
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlin.math.roundToInt


class WalkScreen : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var currentLocation: Location
    private lateinit var oldLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private var locationRequest = createLocationRequest()
    val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var locationCallback: LocationCallback
    var requestingLocationUpdates = true
    var firstCheck = true

    var energyMax = 300
    var energyAmount = energyMax

    //energyBar.max = energyMax
    //energyBar.progress = energyAmount


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
        val energyBar: ProgressBar = findViewById(R.id.energy_mainact_slider)
        energyBar.max = energyMax
        energyBar.progress = energyAmount
        createLocationRequest()
        startLocationUpdates()

        mainHandler.post(object : Runnable {
            override fun run() {
                startLocationUpdates()
                energyBar.progress = energyAmount
                mainHandler.postDelayed(this, 4000)
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
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                permissionCode
            )
            return

        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        val getLocation =
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                override fun isCancellationRequested() = false

                })
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        currentLocation = location
                        if (firstCheck == true) {
                            firstCheck = false
                            oldLocation = currentLocation
                        }
                        else {
                            calculateEnergy()
                        }

                        val mapFragment =
                            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
                        mapFragment.getMapAsync(this)

                        /*val toast = Toast.makeText(this, "test", Toast.LENGTH_SHORT)
                        toast.show()*/



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
                startLocationUpdates()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.clear()
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("Sijainti").icon(
            BitmapDescriptorFactory.fromResource(R.drawable.lechita0030))


        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        googleMap?.addMarker(markerOptions)
        mGoogleMap = googleMap

    }

    fun createLocationRequest(): LocationRequest =

        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .apply {
                setWaitForAccurateLocation(false)
                setMaxUpdateDelayMillis(2000)
            }.build()

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }
    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        requestingLocationUpdates = false;
    }
    private fun calculateEnergy() {
        //figure out how to check last location
        //use haversine formula to calculate distance between old location and new one
        //reduce energy based on result
       /* newLat = currentLocation.latitude
        newLon = currentLocation.longitude*/
        if (oldLocation != currentLocation)
        {
            val distance = currentLocation.distanceTo(oldLocation).roundToInt()
            oldLocation = currentLocation
            energyAmount -= distance
            Toast.makeText(this, energyAmount.toString(), Toast.LENGTH_SHORT).show()

        }
        else {
            //should do nothing  but never actually happens

        }

        //val distance = oldLocation.distanceTo(currentLocation)
        //Toast.makeText(this, "perkele", Toast.LENGTH_SHORT).show()
       // val toast = Toast.makeText(this, distance.toString(), Toast.LENGTH_SHORT).show()



}



}




