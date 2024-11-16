import android.util.Log
import com.ulpgc.uniMatch.data.application.events.Event
import com.ulpgc.uniMatch.data.application.events.EventBus
import com.ulpgc.uniMatch.data.domain.enum.*
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import com.ulpgc.uniMatch.data.infrastructure.events.AppNotificationEvent
import com.ulpgc.uniMatch.data.infrastructure.events.EventNotificationEvent
import com.ulpgc.uniMatch.data.infrastructure.events.MatchNotificationEvent
import com.ulpgc.uniMatch.data.infrastructure.events.MessageNotificationEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject

class NotificationSocket(
    private val backendUrl: String,
    private val notificationPort: Int,
    private val userId: String,
    private val eventBus: EventBus,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    private val client = OkHttpClient()
    private var notificationSocket: WebSocket? = null

    fun connect() {
        val notificationRequest = Request.Builder()
            .url("ws://$backendUrl:$notificationPort/notification/$userId")
            .build()

        notificationSocket = client.newWebSocket(notificationRequest, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.i("NotificationSocket", "Notification WebSocket connected for user $userId")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                parseNotificationMessage(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("NotificationSocket", "Notification WebSocket error: ${t.message}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("NotificationSocket", "Notification WebSocket closed: $reason")
            }
        })
    }

    private fun parseNotificationMessage(text: String) {
        try {
            val notificationResponse = JSONObject(text)
            val type = notificationResponse.getString("type")
            val status = notificationResponse.getString("status")
            val payload = notificationResponse.getString("payload")
            val id = notificationResponse.getString("id")
            val contentId = notificationResponse.getString("contentId")
            val date = notificationResponse.getString("date")
            val recipient = notificationResponse.getString("recipient")

            val typeEnum = NotificationTypeEnum.entries.find { it.type == type }
            val statusEnum = NotificationStatus.entries.find { it.status == status }
            val convertedDate = date.toLong()

            typeEnum?.let {
                when (it) {
                    NotificationTypeEnum.MESSAGE -> handleMessageNotification(id, contentId, recipient, payload, statusEnum, convertedDate)
                    NotificationTypeEnum.MATCH -> handleMatchNotification(id, contentId, recipient, payload, statusEnum, convertedDate)
                    NotificationTypeEnum.APP -> handleAppNotification(id, contentId, recipient, payload, statusEnum, convertedDate)
                    NotificationTypeEnum.EVENT -> handleEventNotification(id, contentId, recipient, payload, statusEnum, convertedDate)
                }
            } ?: Log.e("NotificationSocket", "Unknown notification type: $type")
        } catch (e: JSONException) {
            Log.e("NotificationSocket", "Error parsing notification message: ${e.message}")
        }
    }

    private fun handleMessageNotification(id: String, contentId: String, recipient: String, payload: String, statusEnum: NotificationStatus?, date: Long) {
        try {
            val messagePayload = JSONObject(payload)
            val messageStatusEnum = MessageStatus.entries.find { it.status == messagePayload.getString("status") }
            val deletedStatus = DeletedMessageStatus.entries.find { it.status == messagePayload.getString("deletedStatus") }

            if (messageStatusEnum == null || deletedStatus == null) {
                Log.e("NotificationSocket", "Error parsing message status")
                return
            }

            val messagePayloadObject = MessageNotificationPayload(
                messagePayload.getString("id"),
                messagePayload.getString("sender"),
                messagePayload.getString("content"),
                messagePayload.optString("thumbnail", null),
                messageStatusEnum,
                deletedStatus
            )

            val notification = statusEnum?.let {
                Notifications(
                    id,
                    NotificationTypeEnum.MESSAGE,
                    it,
                    contentId,
                    payload = messagePayloadObject,
                    date = date,
                    recipient
                )
            }

            notification?.let {
                emitEvent(MessageNotificationEvent(it))
            }
        } catch (e: JSONException) {
            Log.e("NotificationSocket", "Error parsing message payload: ${e.message}")
        }
    }

    private fun handleMatchNotification(id: String, contentId: String, recipient: String, payload: String, statusEnum: NotificationStatus?, date: Long) {
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
                Notifications(
                    id,
                    NotificationTypeEnum.MATCH,
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

    private fun handleAppNotification(id: String, contentId: String, recipient: String, payload: String, statusEnum: NotificationStatus?, date: Long) {
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
                Notifications(
                    id,
                    NotificationTypeEnum.APP,
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

    private fun handleEventNotification(id: String, contentId: String, recipient: String, payload: String, statusEnum: NotificationStatus?, date: Long) {
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
                Notifications(
                    id,
                    NotificationTypeEnum.EVENT,
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
