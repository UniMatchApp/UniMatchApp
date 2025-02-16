package com.ulpgc.uniMatch.data.infrastructure.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Location
import com.ulpgc.uniMatch.data.domain.models.Survey
import java.util.Date


@Entity(tableName = "events")
data class EventEntity (
    @PrimaryKey val id: String,
    val title: String,
    val price: Double,
    val latitude: Double?,
    val longitude: Double?,
    val date: Date,
    val ownerId: String,
    val participants: List<String>,
    val likes: List<String>,
    val attachment: String,
    val surveys: List<Survey>
) {
    companion object {
        fun fromDomain(event: Event): EventEntity {
            return EventEntity(
                id = event.eventId,
                title = event.title,
                price = event.price,
                latitude = event.location.latitude,
                longitude = event.location.longitude,
                date = event.date,
                ownerId = event.ownerId,
                participants = event.participants,
                likes = event.likes,
                attachment = event.attachment,
                surveys = event.surveys
            )
        }

        fun toDomain(eventEntity: EventEntity): Event {
            return Event(
                eventId = eventEntity.id,
                title = eventEntity.title,
                price = eventEntity.price,
                location = Location(eventEntity.latitude, eventEntity.longitude, null),
                date = eventEntity.date,
                ownerId = eventEntity.ownerId,
                participants = eventEntity.participants,
                likes = eventEntity.likes,
                attachment = eventEntity.attachment,
                surveys = eventEntity.surveys
            )
        }
    }

}