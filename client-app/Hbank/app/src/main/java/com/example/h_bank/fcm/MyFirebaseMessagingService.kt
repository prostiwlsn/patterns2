package com.example.h_bank.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.h_bank.R
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.useCase.GetUserIdUseCase
import com.example.h_bank.domain.useCase.SendFcmTokenUseCase
import com.example.h_bank.presentation.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val sendFcmTokenUseCase: SendFcmTokenUseCase by inject()
    private val getUserIdUseCase: GetUserIdUseCase by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: "HBank Notification"
            val body = notification.body ?: ""
            sendNotification(title, body)
        } ?: run {
        }

        if (remoteMessage.data.isNotEmpty()) {
        } else {
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendFcmTokenToServer(token)
    }

    private fun sendNotification(title: String, message: String) {
        val channelId = "hbank_notifications"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "HBank Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Канал для уведомлений HBank"
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
            val userId = getUserIdUseCase()
            if (userId != null) {
                when (val result = sendFcmTokenUseCase(userId, isManager = false, token)) {
                    is RequestResult.Success<Unit> -> {
                    }

                    is RequestResult.Error -> {
                    }

                    is RequestResult.NoInternetConnection -> {
                    }
                }
            } else {
            }
        }
    }
}