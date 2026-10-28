package com.example.tp4_ismail

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvLocation: TextView
    private lateinit var btnGetLocation: Button

    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLocation = findViewById(R.id.tvLocation)
        btnGetLocation = findViewById(R.id.btnGetLocation)

        // 1. Initialiser le client de localisation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 2. Déclencher la vérification au clic du bouton
        btnGetLocation.setOnClickListener {
            checkAndGetLocation()
        }
    }

    private fun checkAndGetLocation() {
        // 3. Vérifier si la permission est accordée
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Si non accordée, demander la permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Si déjà accordée, récupérer la position
            fetchCurrentLocation()
        }
    }

    private fun fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    tvLocation.text = "Latitude: $latitude\nLongitude: $longitude"
                } else {
                    Toast.makeText(this, "Unable to find location. Turn on GPS.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 4. Gérer le résultat de la popup de permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation()
            } else {
                Toast.makeText(this, "Permission denied by user", Toast.LENGTH_SHORT).show()
            }
        }
    }
}