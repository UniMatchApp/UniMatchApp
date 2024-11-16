import android.util.Log
import com.ulpgc.uniMatch.data.application.events.EventBus
import com.ulpgc.uniMatch.data.application.services.UserStatusService
import com.ulpgc.uniMatch.data.infrastructure.events.GetUserStatusEvent
import com.ulpgc.uniMatch.data.infrastructure.events.StoppedTypingEvent
import com.ulpgc.uniMatch.data.infrastructure.events.TypingEvent
import com.ulpgc.uniMatch.data.infrastructure.events.UserOfflineEvent
import com.ulpgc.uniMatch.data.infrastructure.events.UserOnlineEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException
import org.json.JSONObject

class UserStatusSocket(
    private val backendUrl: String,
    private val statusPort: Int,
    private val userId: String,
    private val eventBus: EventBus,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : UserStatusService {

    private val client = OkHttpClient()
    private var statusSocket: WebSocket? = null


    fun connect() {
        val statusRequest = Request.Builder()
            .url("ws://$backendUrl:$statusPort/status/$userId")
            .build()

        statusSocket = client.newWebSocket(statusRequest, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.i("UserStatusSocket", "Status WebSocket connected for user $userId")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val statusResponse = JSONObject(text)
                    val chatStatus = statusResponse.getString("type")

                    when (chatStatus) {
                        "userOnline" -> {
                            val userId = statusResponse.getString("userId")
                            coroutineScope.launch {
                                val event = UserOnlineEvent(userId)
                                eventBus.emitEvent(event)
                            }
                        }

                        "userOffline" -> {
                            val userId = statusResponse.getString("userId")
                            coroutineScope.launch {
                                val event = UserOfflineEvent(userId)
                                eventBus.emitEvent(event)
                            }
                        }

                        "typing" -> {
                            val userId = statusResponse.getString("userId")
                            val targetUserId = statusResponse.getString("targetUserId")
                            coroutineScope.launch {
                                val event = TypingEvent(userId, targetUserId)
                                eventBus.emitEvent(event)
                            }
                        }

                        "stoppedTyping" -> {
                            val userId = statusResponse.getString("userId")
                            coroutineScope.launch {
                                val event = StoppedTypingEvent(userId)
                                eventBus.emitEvent(event)
                            }
                        }

                        "getUserStatus" -> {
                            val userId = statusResponse.getString("userId")
                            val status = statusResponse.getString("status")
                            coroutineScope.launch {
                                val event = GetUserStatusEvent(userId, status)
                                eventBus.emitEvent(event)
                            }
                        }
                    }
                } catch (e: JSONException) {
                    Log.e("UserStatusSocket", "Error parsing status message: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("UserStatusSocket", "Status WebSocket error: ${t.message}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("UserStatusSocket", "Status WebSocket closed: $reason")
            }
        })
    }


    private fun sendMessage(socket: WebSocket?, message: String) {
        socket?.send(message)
    }

    override suspend fun setUserTyping(userId: String, targetUserId: String): Result<Unit> {
        val message = """
            {
                "type": "typing",
                "userId": "$userId",
                "targetUserId": "$targetUserId"
            }
        """.trimIndent()
        sendMessage(statusSocket, message)
        return Result.success(Unit)
    }

    override suspend fun setUserStoppedTyping(userId: String): Result<Unit> {
        val message = """
            {
                "type": "stoppedTyping",
                "userId": "$userId"
            }
        """.trimIndent()
        sendMessage(statusSocket, message)
        return Result.success(Unit)
    }

    override suspend fun getUserStatus(userId: String, targetUserId: String): Result<Unit> {
        val message = """
            {
                "type": "getUserStatus",
                "userId": "$userId",
                "targetUserId": "$targetUserId"
            }
        """.trimIndent()
        sendMessage(statusSocket, message)
        return Result.success(Unit)
    }
}
