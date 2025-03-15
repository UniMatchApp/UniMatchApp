package com.ulpgc.uniMatch.data.infrastructure.services.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enums.NotificationTypeEnum
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var profileService: ProfileService

    @SuppressLint("WrongThread")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val notificationData = remoteMessage.data

            Log.i("Notification", "Data: $notificationData")

            val notificationId = notificationData["id"]?.let { generateNotificationId(it) } ?: 0
            val notificationTitle = remoteMessage.data["title"]
            val notificationBody = remoteMessage.data["body"]
            val notificationType = notificationData["type"]
            val action = notificationData["action"]

            if (action == "edit") {
                val oldId = notificationData["oldId"]?.let { generateNotificationId(it) } ?: 0
                removeNotification(oldId)
            }

            if (action == "delete") {
                Log.i("Notification", "Removing notification")
                removeNotification(notificationId)
                return
            }

            // Crear el canal de notificación
            createNotificationChannel()

            // Iniciar trabajo en segundo plano con WorkManager
            val workRequest = OneTimeWorkRequest.Builder(ProfileWork::class.java)
                .setInputData(createInputDataForProfile(notificationData))
                .build()

            WorkManager.getInstance(this).enqueue(workRequest)

            Log.i("WorkManager", "Work enqueued")

            // Espera activa para que el trabajo se complete
            var workInfo = WorkManager.getInstance(this)
                .getWorkInfoById(workRequest.id)
                .get()

            while (workInfo?.state != WorkInfo.State.SUCCEEDED && workInfo?.state != WorkInfo.State.FAILED) {
                Thread.sleep(100)
                workInfo = WorkManager.getInstance(this)
                    .getWorkInfoById(workRequest.id)
                    .get()
            }

            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                Log.i("WorkManager", "Work finished")
                val outputData = workInfo.outputData
                Log.i("WorkManager", "Output data: $outputData")
                val profileName = outputData.getString("profileName")
                Log.i("WorkManager", "Profile name: $profileName")
                Log.i("WorkManager", "Notification type: $notificationType")
                val modifiedBody = generateModifiedBody(notificationType!!, profileName!!, notificationBody!!)
                showNotification(notificationTitle, modifiedBody, notificationId)
            } else {
                Log.e("WorkManager", "Work failed")
            }
        }
    }

    private fun generateModifiedBody(notificationType: String, profileName: String, notificationBody: String): String {
        return when (notificationType) {
            NotificationTypeEnum.MESSAGE.toString() -> "$profileName: $notificationBody"
            NotificationTypeEnum.MATCH.toString() -> "$profileName has matched with you!"
            else -> notificationBody
        }
    }

    private fun createNotificationChannel() {
        val channelId = "default_channel"
        val channelName = "Default Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "This is the default notification channel"
        }

        val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun createInputDataForProfile(notificationData: Map<String, String>): Data {
        return Data.Builder()
            .putString("sender", notificationData["sender"])
            .build()
    }

    private fun showNotification(title: String?, body: String?, notificationId: Int) {
        val notificationManager = NotificationManagerCompat.from(this)

        val notification = NotificationCompat.Builder(this, "default_channel")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.unimatch_logo)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        notificationManager.notify(notificationId, notification)
    }

    private fun removeNotification(notificationId: Int) {
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.cancel(notificationId)
    }

    fun generateNotificationId(notificationId: String): Int {
        return notificationId.hashCode()
    }

}




@HiltWorker
class ProfileWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val profileService: ProfileService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val sender = inputData.getString("sender") ?: return Result.failure()

        return try {
            val result = profileService.getProfile(sender)

            Log.i("ProfileWork", "Profile result: $result")

            if (result.isSuccess) {
                val profileData = result.getOrNull()?.let { profile ->
                    Data.Builder()
                        .putString("profileName", profile.name)
                        .build()
                }
                Result.success(profileData ?: Data.EMPTY)
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e("ProfileWork", "Error getting profile", e)
            Result.failure()
        }
    }
}







