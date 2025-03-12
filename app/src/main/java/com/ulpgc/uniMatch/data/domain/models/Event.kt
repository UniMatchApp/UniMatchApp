package com.ulpgc.uniMatch.data.domain.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Event (
    val eventId: String,
    val title: String,
    val price: Double?,
    val location: Location?,
    val date: Date,
    val ownerId: String,
    val participants: List<String>,
    val likes: List<String>,
    val attachment: String?,
    val surveys: List<Survey>
) {
    fun toDomainModel(): Event {
        return Event(
            eventId = eventId,
            title = title,
            price = price,
            location = location,
            date = date,
            ownerId = ownerId,
            participants = participants,
            likes = likes,
            attachment = attachment,
            surveys = surveys
        )
    }
}

data class SelectSurveyDTO(
    @SerializedName("title") var title: String,
    @SerializedName("option") var option: String
) {
    companion object {
        fun create(
            title: String,
            option: String
        ): SelectSurveyDTO {
            return SelectSurveyDTO(
                title = title,
                option = option
            )
        }
    }
}
