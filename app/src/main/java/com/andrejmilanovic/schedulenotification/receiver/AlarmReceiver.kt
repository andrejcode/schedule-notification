package com.andrejmilanovic.schedulenotification.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.andrejmilanovic.schedulenotification.R
import com.andrejmilanovic.schedulenotification.util.Constants.CHANNEL_ID
import com.andrejmilanovic.schedulenotification.util.Constants.MESSAGE_EXTRA
import com.andrejmilanovic.schedulenotification.util.Constants.NOTIFICATION_ID
import com.andrejmilanovic.schedulenotification.util.Constants.TITLE_EXTRA

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(TITLE_EXTRA))
            .setContentText(intent.getStringExtra(MESSAGE_EXTRA))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder)
    }
}