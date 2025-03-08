import android.util.Log
import com.google.gson.Gson
import com.ulpgc.uniMatch.data.application.events.Event
import com.ulpgc.uniMatch.data.application.events.EventBus
import com.ulpgc.uniMatch.data.domain.enums.ContentStatus
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.EventStatus
import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus
import com.ulpgc.uniMatch.data.domain.enums.NotificationTypeEnum
import com.ulpgc.uniMatch.data.domain.models.notification.Notification
import com.ulpgc.uniMatch.data.infrastructure.events.AppNotificationEvent
import com.ulpgc.uniMatch.data.infrastructure.events.EventNotificationEvent
import com.ulpgc.uniMatch.data.infrastructure.events.MatchNotificationEvent
import com.ulpgc.uniMatch.data.infrastructure.events.MessageNotificationEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException
import org.json.JSONObject
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicBoolean

class NotificationSocket(
    private val backendUrl: String,
    private val notificationPort: Int,
    private val userId: String,
    private val eventBus: EventBus,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    private val client = OkHttpClient()
    private var notificationSocket: WebSocket? = null
    private var isReconnecting = AtomicBoolean(false)
    private val RECONNECT_DELAY_MS = 5000L // 5 segundos

    fun connect() {
        val notificationRequest = Request.Builder()
            .url("ws://$backendUrl:$notificationPort/notification/$userId")
            .build()

        notificationSocket = client.newWebSocket(notificationRequest, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.i("NotificationSocket", "Notification WebSocket connected for user $userId")
                isReconnecting.set(false)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.i("NotificationSocket", "Notification WebSocket message received: $text")
                parseNotificationMessage(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("NotificationSocket", "Notification WebSocket error: ${t.message}")
                attemptReconnect()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("NotificationSocket", "Notification WebSocket closed: $reason")
                attemptReconnect()
            }
        })
    }


    private fun attemptReconnect() {
        if (isReconnecting.get()) return

        isReconnecting.set(true)
        coroutineScope.launch {
            while (isReconnecting.get()) {
                Log.i("NotificationSocket", "Attempting to reconnect...")
                notificationSocket?.cancel()
                connect()
                delay(RECONNECT_DELAY_MS)
            }
        }
    }


    private fun parseNotificationMessage(text: String) {
        try {
            val notificationResponse = JSONObject(text)
            Log.d("NotificationSocket", "Notification Response: $notificationResponse")

            val id = notificationResponse.getString("id")
            val contentId = notificationResponse.getString("contentId")
            val date = notificationResponse.getString("date")
            val recipient = notificationResponse.getString("recipient")
            val status = notificationResponse.getString("status")
            val payload = notificationResponse.getJSONObject("payload")
            val type = payload.getString("type")

            val typeEnum = NotificationTypeEnum.entries.find { it.type == type }
            val statusEnum = NotificationStatus.entries.find { it.status == status }

            val convertedDate = ZonedDateTime.parse(
                date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            ).toInstant().toEpochMilli()

            typeEnum?.let {
                when (it) {
                    NotificationTypeEnum.MESSAGE -> handleMessageNotification(
                        id,
                        contentId,
                        recipient,
                        payload.toString(),
                        statusEnum,
                        convertedDate
                    )

                    NotificationTypeEnum.MATCH -> handleMatchNotification(
                        id,
                        contentId,
                        recipient,
                        payload.toString(),
                        statusEnum,
                        convertedDate
                    )

                    NotificationTypeEnum.APP -> handleAppNotification(
                        id,
                        contentId,
                        recipient,
                        payload.toString(),
                        statusEnum,
                        convertedDate
                    )

                    NotificationTypeEnum.EVENT -> handleEventNotification(
                        id,
                        contentId,
                        recipient,
                        payload.toString(),
                        statusEnum,
                        convertedDate
                    )
                }
            } ?: Log.e("NotificationSocket", "Unknown notification type: $type")
        } catch (e: JSONException) {
            Log.e(
                "NotificationSocket",
                "JSONException -> Error parsing notification message: ${e.message}"
            )
        } catch (e: Exception) {
            Log.e(
                "NotificationSocket",
                "Exception -> Error parsing notification message: ${e.message}"
            )
        }
    }


    private fun handleMessageNotification(
        id: String,
        messageId: String,
        recipient: String,
        payload: String,
        statusEnum: NotificationStatus?,
        date: Long
    ) {
        try {
            val messagePayload = JSONObject(payload)
            val gson = Gson()

            val receptionStatusEnum =
                ReceptionStatus.entries.find { it.status == messagePayload.getString("receptionStatus") }
            val contentStatus =
                ContentStatus.entries.find { it.status == messagePayload.getString("contentStatus") }

            val deletedStatus =
                DeletedMessageStatus.entries.find { it.status == messagePayload.getString("deletedStatus") }

            if (receptionStatusEnum == null || contentStatus == null || deletedStatus == null) {
                Log.e("NotificationSocket", "Error parsing message status")
                return
            }

            Log.i("NotificationSocket", "Message Notification Payload: $messagePayload")

            val messagePayloadObject = MessageNotificationPayload(
                id = messagePayload.getString("id"),
                sender = messagePayload.getString("sender"),
                recipient = recipient,
                content = messagePayload.getString("content"),
                thumbnail = messagePayload.optString("thumbnail", ""),
                receptionStatus = receptionStatusEnum,
                contentStatus = contentStatus,
                deletedStatus = deletedStatus
            )

            val notification = statusEnum?.let {
                Notification(
                    id = id,
                    status = it,
                    contentId = messageId,
                    payload = messagePayloadObject,
                    date = date,
                    recipient = recipient
                )
            }

            notification?.let {
                emitEvent(MessageNotificationEvent(it))
            }
        } catch (e: JSONException) {
            Log.e("NotificationSocket", "Error parsing message payload: ${e.message}")
        }
    }


    private fun handleMatchNotification(
        id: String,
        contentId: String,
        recipient: String,
        payload: String,
        statusEnum: NotificationStatus?,
        date: Long
    ) {
        try {
            val matchPayload = JSONObject(payload)
            val userMatched = matchPayload.getString("userMatched")
            val isLiked = matchPayload.getBoolean("isLiked")

            val matchPayloadObject = MatchNotificationPayload(
                matchPayload.getString("id"),
                userMatched,
                isLiked
            )

            val notification = statusEnum?.let {
                Notification(
                    id,
                    it,
                    contentId,
                    payload = matchPayloadObject,
                    date = date,
                    recipient
                )
            }



            notification?.let {
                emitEvent(MatchNotificationEvent(it))
            }
        } catch (e: JSONException) {
            Log.e("NotificationSocket", "Error parsing match payload: ${e.message}")
        }
    }

    private fun handleAppNotification(
        id: String,
        contentId: String,
        recipient: String,
        payload: String,
        statusEnum: NotificationStatus?,
        date: Long
    ) {
        try {
            val appPayload = JSONObject(payload)
            val title = appPayload.getString("title")
            val description = appPayload.getString("description")

            val appPayloadObject = AppNotificationPayload(
                appPayload.getString("id"),
                title,
                description
            )

            val notification = statusEnum?.let {
                Notification(
                    id,
                    it,
                    contentId,
                    payload = appPayloadObject,
                    date = date,
                    recipient
                )
            }

            notification?.let {
                emitEvent(AppNotificationEvent(it))
            }
        } catch (e: JSONException) {
            Log.e("NotificationSocket", "Error parsing app payload: ${e.message}")
        }
    }

    private fun handleEventNotification(
        id: String,
        contentId: String,
        recipient: String,
        payload: String,
        statusEnum: NotificationStatus?,
        date: Long
    ) {
        try {
            val eventPayload = JSONObject(payload)
            val title = eventPayload.getString("title")
            val status = EventStatus.entries.find { it.status == eventPayload.getString("status") }

            if (status == null) {
                Log.e("NotificationSocket", "Error parsing event status")
                return
            }

            val eventPayloadObject = EventNotificationPayload(
                eventPayload.getString("id"),
                title,
                status
            )

            val notification = statusEnum?.let {
                Notification(
                    id,
                    it,
                    contentId,
                    payload = eventPayloadObject,
                    date = date,
                    recipient
                )
            }

            notification?.let {
                emitEvent(EventNotificationEvent(it))
            }
        } catch (e: JSONException) {
            Log.e("NotificationSocket", "Error parsing event payload: ${e.message}")
        }
    }

    private fun emitEvent(event: Event) {
        coroutineScope.launch {
            eventBus.emitEvent(event)
        }
    }
}
