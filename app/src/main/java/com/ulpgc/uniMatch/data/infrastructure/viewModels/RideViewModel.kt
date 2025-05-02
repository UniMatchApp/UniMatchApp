package com.ulpgc.uniMatch.data.infrastructure.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.api.PlacesApi
import com.ulpgc.uniMatch.data.domain.Place
import com.ulpgc.uniMatch.data.domain.models.Location
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RideViewModel(private val apiKey: String) : ViewModel() {

    var pickUp by mutableStateOf(TextFieldValue(text = ""))
        private set

    private val placesApi = PlacesApi()

    var location by mutableStateOf<Location?>(null)
        private set

    val pickupLocationPlaces: StateFlow<List<Place>> =
        snapshotFlow { pickUp }
            .mapLatest { value ->
                withContext(Dispatchers.IO) {
                    placesApi.fetchPlaces(
                        key = apiKey,
                        input = value.text
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun onPickUpValueChanged(value: TextFieldValue) {
        pickUp = value
        Log.i("RideViewModel", "Pickup value changed: ${pickUp.text}")
    }

    var selectedPlace by mutableStateOf<Place?>(null)
        private set

    fun onPlaceClick(placeId: String) {
        viewModelScope.launch {
            val place = placesApi.fetchPlaceWithCoordinates(
                key = apiKey,
                placeId = placeId,
                name = pickUp.text
            )
            selectedPlace = place
            this@RideViewModel.location = Location(
                latitude = place.latitud,
                longitude = place.longitud,
                0.0
            )
            onPickUpValueChanged(TextFieldValue(text = place.name))
        }
    }
}
