package com.ulpgc.uniMatch.ui.screens.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import java.util.Locale


class LocationHelper(context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    init {
        Companion.context = context.applicationContext
    }

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
        fun getAddressFromCoordinates(latitude: Double, longitude: Double): String? {
            val geocoder = Geocoder(context, Locale.getDefault())
            return try {
                val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1) ?: emptyList()
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    address.getAddressLine(0) // Dirección completa
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
