package com.premium.app.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

// Importa la actividad principal de tu aplicación para abrirla al tocar la notificación
// import com.premium.app.MainActivity // Asume que tienes una MainActivity

class MessagingService : FirebaseMessagingService() {

    private val TAG = "MessagingService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Comprueba si el mensaje contiene una carga de datos.
        remoteMessage.data.isNotEmpty().let { 
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            // Maneja el mensaje de datos aquí.
            // Por ejemplo, puedes extraer información y mostrar una notificación.
        }

        // Comprueba si el mensaje contiene una carga de notificación.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            it.body?.let { body -> sendNotification(body) }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // Envía este token a tu servidor de aplicaciones.
        sendRegistrationToServer(token)
    }

    private fun sendNotification(messageBody: String) {
        // Aquí deberías especificar la actividad que quieres abrir al tocar la notificación.
        // Por ahora, usaremos una Intent genérica que no abrirá nada específico.
        // Reemplaza 'MainActivity::class.java' con tu actividad principal real.
        val intent = Intent(this, Class.forName("com.premium.app.MainActivity")) // Placeholder
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Reemplaza con tu icono de notificación
            .setContentTitle("Premium App Notification")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Desde Android 8.0 (Oreo) se requiere un canal de notificación.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID de notificación */, notificationBuilder.build())
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implementar este método para enviar el token a tu servidor de aplicaciones.
        Log.d(TAG, "sendRegistrationToServer($token)")
    }
}
