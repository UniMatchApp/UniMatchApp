package com.ulpgc.uniMatch.data.infrastructure.services.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ulpgc.uniMatch.MainActivity
import com.ulpgc.uniMatch.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {

            val notificationTitle = remoteMessage.notification?.title
            val notificationBody = remoteMessage.notification?.body
            val notificationData = remoteMessage.data

            // Crear el canal de notificación
            val channelId = "default_channel"
            val channelName = "Default Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "This is the default notification channel"
            }

            // Registra el canal de notificación en el sistema
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(this, PermissionRequestActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                return
            }

            // Si ya tenemos el permiso, mostrar la notificación
            showNotification(notificationTitle, notificationBody, notificationData)
        }
    }

    //TODO: Implementar la lógica de la notificación dependiendo del tipo de notificación recibida para que vaya a la pantalla correspondiente
    @SuppressLint("ServiceCast")
    private fun showNotification(title: String?, body: String?, notificationData: MutableMap<String, String>) {
        val notificationManager = NotificationManagerCompat.from(this)
        val notificationType = notificationData["type"]
        val route = ""

        // Crear un Intent dependiendo de la ruta
        val intent = when (route) {
            else -> Intent(this, MainActivity::class.java)
        }

        // Crear un PendingIntent que se ejecutará cuando el usuario toque la notificación
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Crear la notificación
        val notification = NotificationCompat.Builder(this, "default_channel")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.unimatch_logo)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Mostrar la notificación
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(0, notification)
    }
}
