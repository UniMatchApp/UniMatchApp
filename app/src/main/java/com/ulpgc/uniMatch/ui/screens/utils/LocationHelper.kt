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
import android.util.Log
import androidx.core.app.ActivityCompat

class LocationHelper(context: Context) {
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
        suspend fun getCurrentLocation(context: Context): Pair<Double, Double>? {
            val locationHelper = LocationHelper(context)
            val location = locationHelper.getCurrentLocation()
            return if (location != null) {
                Pair(location.latitude, location.longitude)
            } else {
                null
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
