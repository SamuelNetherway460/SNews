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
        private const val CHANNEL_ONE_NAME = "Notification Channel One"
        private const val CHANNEL_TWO_ID = "com.example.samuelnetherway.snews.TWO"
        private const val CHANNEL_TWO_NAME = "Category Notification Channel"
    }

    private var notificationManager: NotificationManager? = null
    private val manager: NotificationManager?
        get() {
            if (notificationManager == null) {
                notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager
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
        val notificationChannelOne = NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME,
                NotificationManager.IMPORTANCE_HIGH)
        notificationChannelOne.enableLights(true)
        notificationChannelOne.lightColor = Color.BLUE
        notificationChannelOne.setShowBadge(true)
        notificationChannelOne.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager!!.createNotificationChannel(notificationChannelOne)

        val categoryNotificationChannel = NotificationChannel(CHANNEL_TWO_ID, CHANNEL_TWO_NAME,
                NotificationManager.IMPORTANCE_HIGH)
        categoryNotificationChannel.enableLights(true)
        categoryNotificationChannel.lightColor = Color.BLUE
        categoryNotificationChannel.setShowBadge(true)
        categoryNotificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager!!.createNotificationChannel(categoryNotificationChannel)
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
     * Sets up and returns notification one.
     *
     * @param title The title of notification one.
     * @param body The body of notification one.
     * @return Notification one.
     */
    fun getCategoryNotification(title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, CHANNEL_TWO_ID)
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