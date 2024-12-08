package com.ulpgc.uniMatch.data.infrastructure.entities

import AppNotificationPayload
import EventNotificationPayload
import MatchNotificationPayload
import MessageNotificationPayload
import NotificationPayload
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.ulpgc.uniMatch.data.domain.enums.NotificationType
import java.lang.reflect.Type

class NotificationPayloadAdapter : JsonDeserializer<NotificationPayload>,
    JsonSerializer<NotificationPayload> {

    override fun serialize(
        src: NotificationPayload?,
        typeOfSrc: Type?,
        context: JsonSerializationContext
    ): JsonElement {
        val jsonObject = JsonObject()

        jsonObject.addProperty("type", src?.javaClass?.simpleName)

        val specificFields = context.serialize(src)
        jsonObject.add("data", specificFields)

        return jsonObject
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): NotificationPayload {
        val jsonObject =
            json?.asJsonObject ?: throw JsonParseException("Invalid JSON for NotificationPayload")

        val type = jsonObject.get("type")?.asString
            ?: throw JsonParseException("Missing 'type' field in NotificationPayload")

        return when (type) {
            NotificationType.APP.toString() -> context.deserialize(
                json,
                AppNotificationPayload::class.java
            )

            NotificationType.EVENT.toString() -> context.deserialize(
                json,
                EventNotificationPayload::class.java
            )

            NotificationType.MATCH.toString() -> context.deserialize(
                json,
                MatchNotificationPayload::class.java
            )

            NotificationType.MESSAGE.toString() -> context.deserialize(
                json,
                MessageNotificationPayload::class.java
            )

            else -> throw JsonParseException("Unknown type: $type")
        }
    }
}
