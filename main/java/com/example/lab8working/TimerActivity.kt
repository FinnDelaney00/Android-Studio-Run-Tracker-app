package com.example.lab8working

// asked gemini how i could implement a distance tracker using fusedLocationClient. output: Location Setup:
//Initialize fusedLocationClient, locationRequest, and locationCallback as in your TimerActivity.
//Location Updates:
//Request location updates in onCreate() after checking for permissions.
//Distance Calculation:
//In onLocationResult(), calculate the distance between the current location and the previous location (startLocation).
//Accumulate the distance in totalDistance.
//Update Item with Distance:
//In the save button's setOnClickListener, include totalDistance when creating the updatedItem object to store the distance in the database.
//Permissions and Lifecycle:
//Handle location permissions in onRequestPermissionsResult().
//Stop location updates in onPause() and onStop() to conserve battery.
//Important Considerations
//UI Updates: If you want to display the distance in real-time while editing, you can add a TextView and update it in onLocationResult().
//Error Handling: Implement error handling for cases where location updates are unavailable or inaccurate.
//Battery Usage: Continuously requesting location updates can consume significant battery power. Consider adjusting the interval and priority of your LocationRequest to balance accuracy and battery usage.

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class TimerActivity : AppCompatActivity() {

    private lateinit var viewItemsButton: ImageButton
    private lateinit var LoginButton: ImageButton
    private lateinit var AccountButton: ImageButton
    private lateinit var LoginButtonTop: ImageButton
    private lateinit var timerTextView: TextView
    private lateinit var stopTimerButton: Button
    private lateinit var locationTextView: TextView
    private lateinit var distanceTextView: TextView
    private var seconds = 0
    private var running = false
    private lateinit var handler: Handler
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var startLocation: Location? = null
    private var totalDistance = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timerTextView = findViewById(R.id.timerTextView)
        stopTimerButton = findViewById(R.id.stopTimerButton)
        locationTextView = findViewById(R.id.locationTextView)
        distanceTextView = findViewById(R.id.distanceTextView)
        viewItemsButton = findViewById(R.id.viewItemButton)
        LoginButton = findViewById(R.id.profileButton)
        LoginButtonTop = findViewById(R.id.profileButtonTop)
        AccountButton = findViewById(R.id.AccountActivityButton)

        viewItemsButton.setOnClickListener {
            val intent = Intent(this, DisplayActivity::class.java)
            startActivity(intent)
        }

        LoginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        LoginButtonTop.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        AccountButton.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

        handler = Handler(Looper.getMainLooper())

        // Location setup
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        locationRequest = LocationRequest.create().apply {
//            interval = 10000
//            fastestInterval = 5000
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).apply {setGranularity(Granularity.GRANULARITY_FINE)
            setMinUpdateIntervalMillis(5000)
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    if (startLocation == null) {
                        startLocation = location
                        Log.d("StartLocation", "Start location set: $startLocation")
                    } else {
                        val distance = startLocation!!.distanceTo(location)
                        totalDistance += distance
                        Log.d("DistanceCalculation", "Distance: $distance, Total Distance: $totalDistance")
                        updateDistanceTextView(totalDistance)
                        startLocation = location // Update startLocation for next calculation
                    }
                    updateLocationTextView(location)
                }
            }
        }

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            // Request location permissions
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        stopTimerButton.setOnClickListener {
            running = false
            val timeData = timerTextView.text.toString()
            val distanceData = totalDistance
            val intent = Intent(this, AddItemActivity::class.java).apply {
                putExtra("Activity Duration", timeData)
                putExtra("Activity Distance", distanceData)
                Log.d("FinnRuns", "$distanceData is being passed")
            }
            onStop()
            startActivity(intent)
        }

        runTimer()
    }

    private fun updateLocationTextView(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        locationTextView.text = "Latitude: $latitude, Longitude: $longitude"
    }

    private fun updateDistanceTextView(distance: Float) {
        distanceTextView.text = "Distance: ${distance} meters"
    }

    private fun runTimer() {
        running = true
        handler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secs = seconds % 60

                val time = String.format("%02d:%02d:%02d", hours, minutes, secs)
                timerTextView.text = time

                if (running) {
                    seconds++
                    handler.postDelayed(this, 1000)
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                Log.d("LocationPermission", "Location permission granted")
                // ... (start location updates) ...
            } else {
                // Permission denied, handle accordingly
                Log.d("LocationPermission", "Location permission denied")
                // ... (display a message or disable location-related features) ...
            }
        }
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
