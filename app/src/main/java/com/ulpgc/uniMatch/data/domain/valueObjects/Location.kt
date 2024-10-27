package com.ulpgc.uniMatch.data.domain.valueObjects

class Location(
    private var _latitude: Double,
    private var _longitude: Double,
    private var _altitude: Double? = null
) {
    init {
        if (_latitude < -90 || _latitude > 90) {
            throw IllegalArgumentException("Latitude must be between -90 and 90 degrees.")
        }
        if (_longitude < -180 || _longitude > 180) {
            throw IllegalArgumentException("Longitude must be between -180 and 180 degrees.")
        }
    }


    var latitude: Double
        get() = _latitude
        private set(value) {
            if (value < -90 || value > 90) {
                throw IllegalArgumentException("Latitude must be between -90 and 90 degrees.")
            }
            _latitude = value
        }

    var longitude: Double
        get() = _longitude
        private set(value) {
            if (value < -180 || value > 180) {
                throw IllegalArgumentException("Longitude must be between -180 and 180 degrees.")
            }
            _longitude = value
        }

    var altitude: Double?
        get() = _altitude
        set(value) {
            _altitude = value
        }

    override fun toString(): String {
        return "Lat: $_latitude, Lon: $_longitude, Alt: ${_altitude ?: "N/A"}"
    }

    companion object {
        fun stringToLocation(location: String): Location {
            val locationArray = location.split(", ")
            val latitude = locationArray[0].split(": ")[1].toDouble()
            val longitude = locationArray[1].split(": ")[1].toDouble()
            val altitudeStr = locationArray[2].split(": ")[1]
            val altitude = if (altitudeStr == "N/A") null else altitudeStr.toDouble()
            return Location(latitude, longitude, altitude)
        }
    }
}