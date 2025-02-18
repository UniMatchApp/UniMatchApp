package com.ulpgc.uniMatch.data.application.api

import android.util.Log
import com.ulpgc.uniMatch.data.application.DTO.PlacesDto
import com.ulpgc.uniMatch.data.domain.Place
import com.ulpgc.uniMatch.data.domain.models.Location
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class PlacesApi {

    private val client: HttpClient = HttpClient {
        install(Logging) {
            level = LogLevel.HEADERS
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v(tag = "PlacesApi", message = message)
                }
            }
        }.also { Log.d("PlacesApi", "Logger initialized") }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
    }
    suspend fun fetchPlaces(key: String, input: String): List<Place> {
        val placesDto: PlacesDto = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "maps.googleapis.com"
                path("maps/api/place/autocomplete/json")
                parameters.append("key", key)
                parameters.append("types", "address")
                parameters.append("input", input)
            }
        }.body()

        return placesDto.toPlacesList()
    }

    suspend fun fetchPlaceWithCoordinates(key: String, placeId: String, name: String): Place {
        val response = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "maps.googleapis.com"
                path("maps/api/place/details/json")
                parameters.append("key", key)
                parameters.append("placeid", placeId)
            }
        }

        val detailsDto = response.bodyAsText()
        val jsonResponse = Json.parseToJsonElement(detailsDto)

        val lat = jsonResponse.jsonObject["result"]?.jsonObject?.get("geometry")?.jsonObject?.get("location")?.jsonObject?.get("lat")?.jsonPrimitive?.double ?: 0.0
        val lng = jsonResponse.jsonObject["result"]?.jsonObject?.get("geometry")?.jsonObject?.get("location")?.jsonObject?.get("lng")?.jsonPrimitive?.double ?: 0.0
        return Place(placeId, name, lat, lng)
    }

}