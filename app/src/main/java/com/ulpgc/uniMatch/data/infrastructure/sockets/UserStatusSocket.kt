import android.util.Log
import com.ulpgc.uniMatch.data.application.services.UserStatusService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class UserStatusSocket(
    private val backendUrl: String,
    private val statusPort: Int,
    private val userId: String
) : UserStatusService {

    private val client = OkHttpClient()
    private var statusSocket: WebSocket? = null

    fun connect() {

        Log.i("UserStatusSocket", "Connecting to WebSocket")

        val statusRequest = Request.Builder()
            .url("ws://$backendUrl:$statusPort/status/?userId=$userId")
            .build()

        statusSocket = client.newWebSocket(statusRequest, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.i("UserStatusSocket", "Status WebSocket connected for user $userId")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.i("UserStatusSocket", "Status received: $text")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("UserStatusSocket", "Status WebSocket error: ${t.message}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("UserStatusSocket", "Status WebSocket closed: $reason")
            }
        })
    }

    fun close() {
        statusSocket?.close(1000, "Client disconnected")
        client.dispatcher.executorService.shutdown()
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

    override suspend fun getUserStatus(userId: String, targetUserId: String): Result<String> {
        val message = """
            {
                "type": "getUserStatus",
                "userId": "$userId",
                "targetUserId": "$targetUserId"
            }
        """.trimIndent()
        sendMessage(statusSocket, message)
        return Result.success("Unknown")
    }
}
