package com.ulpgc.uniMatch.ui.screens.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import android.Manifest
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import android.location.Address
import java.util.Locale

class LocationHelper(context: Context) {

    init {
        Companion.context = context.applicationContext
    }


    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Constante privada, accesible solo en esta clase
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        return try {
            fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            null
        }
    }

    companion object {

        private lateinit var context: Context

        suspend fun getCurrentLocation(context: Context): Pair<Double, Double>? {
            val locationHelper = LocationHelper(context)
            val location = locationHelper.getCurrentLocation()
            return if (location != null) {
                Pair(location.latitude, location.longitude)
            } else {
                null
            }

        }

        fun getAddressFromCoordinates(latitude: Double?, longitude: Double?): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            return try {
                if (latitude != null && longitude != null) {
                    val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1) ?: emptyList()
                    if (addresses.isNotEmpty()) {
                        addresses[0].getAddressLine(0) ?: "No establecido"
                    } else {
                        "No establecido"
                    }
                } else {
                    "No establecido"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "No establecido"
            }
        }


        fun checkLocationPermission(context: Context): Boolean {
            Log.i("LocationHelper", "Checking location permission ${ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED}")

            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}
