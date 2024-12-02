package com.ulpgc.uniMatch.data.infrastructure.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PermissionsViewModel()  : ViewModel() {

    private val _hasStoragePermissions = MutableStateFlow(false)
    val hasStoragePermissions: StateFlow<Boolean> get() = _hasStoragePermissions

    private val _hasLocationPermissions = MutableStateFlow(false)
    val hasLocationPermissions: StateFlow<Boolean> get() = _hasLocationPermissions

    fun updateLocationPermissionStatus(isGranted: Boolean) {
        _hasLocationPermissions.value = isGranted
    }

    fun updateStoragePermissionStatus(isGranted: Boolean) {
        _hasStoragePermissions.value = isGranted
    }


}