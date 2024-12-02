package com.ulpgc.uniMatch.data.infrastructure.viewModels

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



class PermissionsViewModel()  : ViewModel() {

    private val _hasStoragePermissions = MutableStateFlow(false)
    val hasStoragePermissions: StateFlow<Boolean> get() = _hasStoragePermissions

    private val _hasLocationPermissions = MutableStateFlow(false)
    val hasLocationPermissions: StateFlow<Boolean> get() = _hasLocationPermissions

    fun updateLocationPermissionStatus(isGranted: Boolean) {
        Log.i("PermissionsViewModel", "Location permission changed: $isGranted")
        _hasLocationPermissions.value = isGranted
        Log.i("PermissionsViewModel", "Location permission changed: ${_hasLocationPermissions.value}")
    }

    fun updateStoragePermissionStatus(isGranted: Boolean) {
        Log.i("PermissionsViewModel", "Storage permission changed: $isGranted")
        _hasStoragePermissions.value = isGranted
        Log.i("PermissionsViewModel", "Storage permission changed: ${_hasStoragePermissions.value}")
    }


}