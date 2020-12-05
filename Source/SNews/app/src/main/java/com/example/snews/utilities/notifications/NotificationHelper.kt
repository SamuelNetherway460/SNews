package com.example.snews.utilities.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.snews.R

/**
 * Helper class for creating and posting notifications to the user.
 * Code snippets taken from Swansea University CS-306 Mobile Apps lecture notes.
 *
 * @param base The application context.
 * @author Samuel Netherway
 */
internal class NotificationHelper  (base: Context) : ContextWrapper(base) {

    /**
     * Companion object for constants.
     */
    companion object {
        private const val CHANNEL_ONE_ID = "com.example.samuelnetherway.snews.ONE"
        private const val CHANNEL_ONE_NAME = "Notifcation Channel One"
    }

    private var notifationManager: NotificationManager? = null
    private val manager: NotificationManager?
        get() {
            if (notifationManager == null) {
                notifationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return notifationManager
        }

    /**
     * Performs basic class initialisation.
     */
    init {
        createNotificationChannels()
    }

    /**
     * Creates the notification channels required by the application.
     */
    private fun createNotificationChannels() {
        val notificationChannel = NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME,
                NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.setShowBadge(true)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager!!.createNotificationChannel(notificationChannel)
    }

    /**
     * Sets up and returns notification one.
     *
     * @param title The title of notification one.
     * @param body The body of notification one.
     * @return Notification one.
     */
    fun getNotificationOne(title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, CHANNEL_ONE_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setNumber(3)
                .setAutoCancel(true)
    }

    /**
     * Notifies the user with a notification.
     *
     * @param id The unique identifier of the notification.
     * @param notification The notification.
     */
    fun notify(id: Int, notification: NotificationCompat.Builder) {
        manager!!.notify(id, notification.build())
    }
}