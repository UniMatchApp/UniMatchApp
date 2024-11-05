import com.ulpgc.uniMatch.data.domain.enum.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enum.EventStatus
import com.ulpgc.uniMatch.data.domain.enum.MessageStatus

abstract class NotificationPayload(
    val id: String
)

class AppNotificationPayload(
    id: String,
    private val title: String,
    private val description: String
) : NotificationPayload(id) {
    fun getTitle(): String {
        return title
    }

    fun getDescription(): String {
        return description
    }
}

class EventNotificationPayload(
    id: String,
    private val title: String,
    private val status: EventStatus,
) : NotificationPayload(id) {
    fun getTitle(): String {
        return title
    }

    fun getStatus(): EventStatus {
        return status
    }
}

class MatchNotificationPayload(
    id: String,
    private val userMatched: String,
    private val isLiked: Boolean
) : NotificationPayload(id) {
    fun getUserMatched(): String {
        return userMatched
    }

    fun isLiked(): Boolean {
        return isLiked
    }
}

class MessageNotificationPayload(
    id: String,
    private val sender: String,
    private val content: String,
    private val thumbnail: String?,
    private val status: MessageStatus,
    private val deletedStatus: DeletedMessageStatus
) : NotificationPayload(id) {
    fun getSender(): String {
        return sender
    }

    fun getContent(): String {
        return content
    }

    fun getThumbnail(): String? {
        return thumbnail
    }

    fun getStatus(): MessageStatus {
        return status
    }

    fun getDeletedStatus(): DeletedMessageStatus {
        return deletedStatus
    }
}
