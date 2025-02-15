package com.ulpgc.uniMatch.data.infrastructure.services.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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

            showNotification(notificationTitle, notificationBody, "")
        }
    }

    @SuppressLint("ServiceCast")
    private fun showNotification(title: String?, body: String?, route: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManagerCompat

        // Crear un Intent dependiendo de la ruta
        val intent = when (route) {
            // Puedes agregar más rutas aquí según sea necesario
            else -> Intent(this, MainActivity::class.java) // Ruta por defecto
        }

        // Añadir datos adicionales al Intent si es necesario
        val bundle = Bundle().apply {
            putString("extra_data", "some_data") // Si necesitas pasar más datos
        }
        intent.putExtras(bundle)

        // Crear un PendingIntent que se ejecutará cuando el usuario toque la notificación
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

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

    override fun onNewToken(token: String) {
        // Este método se llama cuando se genera un nuevo token FCM
        // Enviar el token al servidor para enviar notificaciones
    }
}

