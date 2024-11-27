package com.ulpgc.uniMatch.data.infrastructure.entities

import AppNotificationPayload
import EventNotificationPayload
import MatchNotificationPayload
import MessageNotificationPayload
import NotificationPayload
import com.google.gson.*
import java.lang.reflect.Type

class NotificationPayloadAdapter : JsonDeserializer<NotificationPayload>, JsonSerializer<NotificationPayload> {

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
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON for NotificationPayload")

        val type = jsonObject.get("type")?.asString
            ?: throw JsonParseException("Missing 'type' field in NotificationPayload")

        val data = jsonObject.getAsJsonObject("data")
        return when (type) {
            AppNotificationPayload::class.java.simpleName -> context.deserialize(data, AppNotificationPayload::class.java)
            EventNotificationPayload::class.java.simpleName -> context.deserialize(data, EventNotificationPayload::class.java)
            MatchNotificationPayload::class.java.simpleName -> context.deserialize(data, MatchNotificationPayload::class.java)
            MessageNotificationPayload::class.java.simpleName -> context.deserialize(data, MessageNotificationPayload::class.java)
            else -> throw JsonParseException("Unknown type: $type")
        }
    }
}
