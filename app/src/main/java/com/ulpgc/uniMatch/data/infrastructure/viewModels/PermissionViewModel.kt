package com.ulpgc.uniMatch.data.infrastructure.viewModels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionsViewModel : ViewModel() {
    private val _hasLocationPermission = MutableStateFlow(false)
    val hasLocationPermission: StateFlow<Boolean> get() = _hasLocationPermission

    private val _hasStoragePermission = MutableStateFlow(false)
    val hasStoragePermission: StateFlow<Boolean> get() = _hasStoragePermission

    fun updateLocationPermissionStatus(isGranted: Boolean) {
        _hasLocationPermission.value = isGranted
    }

    private fun updateStoragePermissionStatus(isGranted: Boolean) {
        _hasStoragePermission.value = isGranted
    }

    fun checkLocationPermission(context: Context) {
        val isGranted = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        updateLocationPermissionStatus(isGranted)
    }

    fun checkStoragePermission(context: Context) {
        val isGranted = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        updateStoragePermissionStatus(isGranted)
    }
}
