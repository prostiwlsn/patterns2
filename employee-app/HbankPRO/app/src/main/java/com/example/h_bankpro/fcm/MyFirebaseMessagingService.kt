package com.example.h_bankpro.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.h_bankpro.R
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.useCase.SendFcmTokenUseCase
import com.example.h_bankpro.presentation.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val sendFcmTokenUseCase: SendFcmTokenUseCase by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: "HBankPro Notification"
            val body = notification.body ?: ""
            sendNotification(title, body)
        } ?: {}
    }

    override fun onNewToken(token: String) {
        sendFcmTokenToServer(token)
    }

    private fun sendNotification(title: String, message: String) {
        val channelId = "hbankpro_notifications"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "HBankPro Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for HBankPro notifications"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        try {
            notificationManager.notify(notificationId, notification)
        } catch (e: Exception) {
        }
    }

    private fun sendFcmTokenToServer(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val userId = "employee_id"
            when (val result = sendFcmTokenUseCase(userId, isManager = true, token)) {
                is RequestResult.Success -> {
                }

                is RequestResult.Error -> {
                }

                is RequestResult.NoInternetConnection -> {
                }
            }
        }
    }
}