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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean

class UserStatusSocket(
    private val backendUrl: String,
    private val statusPort: Int,
    private val userId: String,
    private val eventBus: EventBus,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : UserStatusService {

    private val client = OkHttpClient()
    private var statusSocket: WebSocket? = null
    private var isReconnecting = AtomicBoolean(false)
    private val RECONNECT_DELAY_MS = 5000L // 5 segundos


    fun connect() {
        statusSocket?.cancel()
        val courutineScope = CoroutineScope(Dispatchers.IO)

        courutineScope.launch {
            val statusRequest = Request.Builder()
                .url("ws://$backendUrl:$statusPort/status/$userId")
                .build()

            statusSocket = client.newWebSocket(statusRequest, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    isReconnecting.set(false) // Reconexión exitosa
                }


                override fun onMessage(webSocket: WebSocket, text: String) {
                    try {
                        Log.i("UserStatusSocket", "Status message received: $text")
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

                                val targetUserId = statusResponse.getString("userId")
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
                    attemptReconnect() // Intentar reconectar
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    Log.i("UserStatusSocket", "Status WebSocket closed: $reason")
                    attemptReconnect() // Intentar reconectar
                }
            })
        }
    }


    private fun attemptReconnect() {
        if (isReconnecting.get()) return

        isReconnecting.set(true)
        coroutineScope.launch {
            while (isReconnecting.get()) {
                Log.i("UserStatusSocket", "Attempting to reconnect...")
                statusSocket?.cancel()
                connect()
                delay(RECONNECT_DELAY_MS) // Espera antes de volver a intentar
            }
        }
    }

    private fun sendMessage(socket: WebSocket?, message: String) {
        socket?.send(message)
    }

    private fun ensureConnected() {
        if (statusSocket == null) {
            connect()
        }
    }

    override suspend fun setUserTyping(userId: String, targetUserId: String): Result<Unit> {
        ensureConnected()
        val message = """{"type": "typing","userId": "$userId","targetUserId": "$targetUserId"}""".trimIndent()
        sendMessage(statusSocket, message)
        return Result.success(Unit)
    }

    override suspend fun setUserStoppedTyping(userId: String): Result<Unit> {
        ensureConnected()
        val message = """{"type": "stoppedTyping","userId": "$userId"}""".trimIndent()
        sendMessage(statusSocket, message)
        return Result.success(Unit)
    }


    override suspend fun getUserStatus(userId: String, targetUserId: String): Result<Unit> {
        ensureConnected()
        val message = """{"type": "getUserStatus","userId": "$userId","targetUserId": "$targetUserId"}""".trimIndent()
        sendMessage(statusSocket, message)
        return Result.success(Unit)
    }

}
