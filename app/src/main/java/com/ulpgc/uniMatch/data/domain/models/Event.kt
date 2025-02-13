package com.ulpgc.uniMatch.data.domain.models

import java.util.Date

data class Event (
    val eventId: String,
    val title: String,
    val price: Double,
    val location: String,
    val date: Date,
    val ownerId: String,
    val participants: List<String>,
    val likes: List<String>,
    val attachment: String,
    val surveys: List<Survey>
)
