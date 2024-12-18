import com.ulpgc.uniMatch.data.domain.enums.ContentStatus
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.EventStatus
import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
import com.ulpgc.uniMatch.data.domain.enums.NotificationType

abstract class NotificationPayload(
    val id: String,
    val type: NotificationType
)

class AppNotificationPayload(
    id: String,
    private val title: String,
    private val description: String
) : NotificationPayload(id, NotificationType.APP) {
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
) : NotificationPayload(id, NotificationType.EVENT) {
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
) : NotificationPayload(id, NotificationType.MATCH) {
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
    private val recipient: String,
    private val content: String,
    private val thumbnail: String?,
    private val receptionStatus: ReceptionStatus,
    private val contentStatus: ContentStatus,
    private val deletedStatus : DeletedMessageStatus
) : NotificationPayload(id, NotificationType.MESSAGE) {
    fun getSender(): String {
        return sender
    }

    fun getRecipient(): String {
        return recipient
    }

    fun getContent(): String {
        return content
    }

    fun getThumbnail(): String? {
        return thumbnail
    }

    fun getReceptionStatus(): ReceptionStatus {
        return receptionStatus
    }

    fun getContentStatus(): ContentStatus {
        return contentStatus
    }

    fun getDeletedStatus() : DeletedMessageStatus {
        return deletedStatus
    }
}
