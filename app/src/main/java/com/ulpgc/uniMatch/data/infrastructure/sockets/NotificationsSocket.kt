import okhttp3.*
import okio.ByteString

class NotificationsSocket(
    private val backendUrl: String,
    private val notificationPort: Int,
    private val userId: String
) {

    private val client = OkHttpClient()
    private var notificationSocket: WebSocket? = null

    fun connect() {
        val notificationRequest = Request.Builder()
            .url("ws://$backendUrl:$notificationPort/notifications/$userId")
            .build()

        notificationSocket = client.newWebSocket(notificationRequest, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("Notification WebSocket connected for user $userId")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("Notification received: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                println("Binary notification received: $bytes")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("Notification WebSocket error: ${t.message}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                println("Notification WebSocket closing: $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("Notification WebSocket closed: $reason")
            }
        })
    }

    fun close() {
        notificationSocket?.close(1000, "User logged out")
    }


}
