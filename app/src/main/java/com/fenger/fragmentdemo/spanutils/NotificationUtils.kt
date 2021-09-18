package com.fenger.fragmentdemo.spanutils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.fenger.fragmentdemo.MyApplication
import com.fenger.fragmentdemo.R

/**
 * @author fengerzhang
 * @date 2021/9/18 10:35
 */
object NotificationUtils {

    private const val CHANNEL_ID = "KANDIAN_NOTIFICATION_CHANNEL_ID"
    private const val NOTIFICATION_TITLE = "标题"
    const val MAX_PROGRESS = 100 // 默认最大进度为100%

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotificationBuilder(
        content: String
    ): NotificationCompat.Builder {
        val channel = NotificationChannel(CHANNEL_ID, MyApplication.instance!!.applicationContext.packageName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.apply {
            enableLights(true)
            lockscreenVisibility = NotificationCompat.VISIBILITY_SECRET
            canShowBadge()
            setSound(null, null)
            setBypassDnd(true)
            shouldShowLights()
        }

        //通知管理者创建的渠道
        val manager = MyApplication.instance!!.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        return NotificationCompat.Builder(MyApplication.instance!!.applicationContext, CHANNEL_ID)
            .setAutoCancel(true)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(content)
            .setSmallIcon(R.drawable.pager_image1)
    }

    fun showNotification(
        content: String,
        manageId: Int,
        progress: Int,
        maxProgress: Int,
        pendingIntent: PendingIntent?= null
    ) {
        // Get the layouts to use in the custom notification
        val notificationLayout = RemoteViews(MyApplication.instance!!.applicationContext.packageName, R.layout.ui_customer_keyboard)

        val notification =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getNotificationBuilder(content).apply {
                setOnlyAlertOnce(true)
                setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)
                setContentIntent(pendingIntent)
                setCustomBigContentView(notificationLayout)
                setWhen(System.currentTimeMillis())
            }.build()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            NotificationCompat.Builder(MyApplication.instance!!.applicationContext, CHANNEL_ID)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(content)
                .setSmallIcon(R.drawable.pager_image1)
                .setContentIntent(pendingIntent)
                .build()
        } else {
            Notification.Builder(MyApplication.instance!!.applicationContext)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.pager_image1)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build()
        }

        val manager = MyApplication.instance!!.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(manageId, notification)
    }

    fun cancelNotification(manageId: Int) {
        val manager = MyApplication.instance!!.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(manageId)
    }
}