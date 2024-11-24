package com.ulpgc.uniMatch.ui.screens.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class LocationHelper(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

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
    }
}

