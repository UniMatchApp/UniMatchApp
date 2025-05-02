package com.ulpgc.uniMatch.data.infrastructure.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RideViewModelFactory(
    private val apiKey: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RideViewModel::class.java)) {
            return RideViewModel(apiKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
