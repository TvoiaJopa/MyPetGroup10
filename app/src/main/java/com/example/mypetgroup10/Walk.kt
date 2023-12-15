package com.example.mypetgroup10

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
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
import kotlin.random.Random


class WalkScreen : AppCompatActivity(), OnMapReadyCallback {

    val rewardInterval = 50
    val rewardChance = 70
    val rewardAmount = 50

    private lateinit var currentLocation: Location
    private lateinit var oldLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private var locationRequest = createLocationRequest()
    val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var locationCallback: LocationCallback
    var requestingLocationUpdates = true
    var firstCheck = true



    var energyMax = 500
    var energyAmount = energyMax
    var rewardDistance = 0
    var rewardTaken = true

    //energyBar.max = energyMax
    //energyBar.progress = energyAmount


    private var mGoogleMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("your_game_prefs", Context.MODE_PRIVATE)
        //Get saved data
        energyMax = sharedPreferences.getInt("energyMax",500)
        energyAmount = sharedPreferences.getInt("energyAmount",energyMax)
        rewardDistance = sharedPreferences.getInt("rewardDistance",0)

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

        //Init energy bar
        val energyBar: ProgressBar = findViewById(R.id.energy_mainact_slider)
        energyBar.max = energyMax
        energyBar.progress = energyAmount
        createLocationRequest()
       // startLocationUpdates()

        //Loop that checks location every 4s and updates energy bar

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

        //Check if we have permission for location, request it if not
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
        //Request location update from play services
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        val getLocation =
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                override fun isCancellationRequested() = false

                })
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        currentLocation = location

                        //Old location starts as null, this is a dirty hack to avoid crash due check of null value
                        if (firstCheck == true) {
                            firstCheck = false
                            oldLocation = currentLocation
                        }
                        //Value is not null, call for energy calculation and reward check
                        else {
                            calculateEnergy()
                        }
                        //Call for gmap update

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
        val sharedPreferences = getSharedPreferences("your_game_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        //Compare old location to current location
        if (oldLocation != currentLocation)
        {
            //calculate distance between old and current locations, update old location to match current one
            val distance = currentLocation.distanceTo(oldLocation).roundToInt()
            oldLocation = currentLocation
            energyAmount -= distance
            //Check if we have enough energy to get rewards, check if enough time has passed for reward check
            if (energyAmount > 0) {
                rewardDistance += distance
                if (rewardDistance > rewardInterval && rewardTaken) {
                    rewardDistance = 0
                    checkForReward()
                }
            }
            else  if (energyAmount<0){
                //If our energy goes to negative
                energyAmount = 0
            }



        }

        //Add +3 to energy

        energyAmount += 2
        editor.putInt("energyAmount",energyAmount)
        editor.putInt("rewardDistance",rewardDistance)
        editor.apply()
        //Toast.makeText(this, energyAmount.toString(), Toast.LENGTH_SHORT).show()

}
    fun checkForReward (){
        //val sharedPreferences = getSharedPreferences("your_game_prefs", Context.MODE_PRIVATE)
        //val editor = sharedPreferences.edit()

        //Rng check if we get reward
        if (Random.nextInt(0, 100) < rewardChance) {
            //give reward
            //var money = sharedPreferences.getInt("money",0)+rewardAmount
            Toast.makeText(this, "Pet found some money!", Toast.LENGTH_SHORT).show()
            spawnReward()
           // editor.putInt("money",money)
           // editor.apply()

        }
    }

    fun spawnReward () {
        rewardTaken = false
        val rewardButton: ImageView = findViewById(R.id.rewardButton)
        rewardButton.setVisibility(
            if (!rewardTaken)View.VISIBLE else View.GONE
        )
        rewardButton.setOnClickListener{ claimReward() }
    }
    fun claimReward() {
        val sharedPreferences = getSharedPreferences("your_game_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        rewardTaken = true
        var money = sharedPreferences.getInt("money",0)+rewardAmount
        val rewardButton: ImageView = findViewById(R.id.rewardButton)
        rewardButton.setVisibility(
            View.GONE
        )
        Toast.makeText(this, "Collected $rewardAmount money", Toast.LENGTH_SHORT).show()
        editor.putInt("money",money)
        editor.apply()
    }




}




