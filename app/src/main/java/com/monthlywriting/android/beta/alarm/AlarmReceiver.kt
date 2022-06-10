package com.monthlywriting.android.beta.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.MainActivity

class AlarmReceiver() : BroadcastReceiver() {

    lateinit var context: Context

    override fun onReceive(context: Context, p1: Intent?) {
        this.context = context

        notifyMessage()
    }

    private fun notifyMessage() {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "monthly writing notification"
            val channelName = "notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(channelId, channelName, importance).apply {
                    description = "Monthly Writing Notification"
                    enableVibration(true)
                    setShowBadge(true)
                }
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(context, channelId)
        } else {
            builder = NotificationCompat.Builder(context)
        }

        builder.apply {
            setSmallIcon(R.drawable.ic_btn_delete)
            setContentTitle(context.resources.getString(R.string.app_name))
            setContentText(context.resources.getString(R.string.notification_message))
            setWhen(System.currentTimeMillis())
            setAutoCancel(true)

            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("isFromAlarm", true)
            val pendingIntent =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.getActivity(context,
                        MONTHLY_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                } else {
                    PendingIntent.getActivity(context,
                        MONTHLY_NOTIFICATION,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }
            setContentIntent(pendingIntent)
        }

        manager.notify(10, builder.build())
    }

    companion object {
        const val MONTHLY_NOTIFICATION = 10
    }
}