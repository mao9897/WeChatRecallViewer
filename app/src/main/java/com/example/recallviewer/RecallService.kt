package com.example.recallviewer

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.app.Notification

class RecallService : NotificationListenerService() {

    private lateinit var db: RecallDatabase

    override fun onCreate() {
        super.onCreate()
        db = RecallDatabase(this)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName != "com.tencent.mm") return

        val notification = sbn.notification ?: return
        val extras = notification.extras ?: return

        val title = extras.getString(Notification.EXTRA_TITLE) ?: return
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: return

        // 过滤撤回提示本身
        if (text.contains("撤回了一条消息") || text.contains("recalled")) return
        if (title == "微信" || title == "WeChat") return

        val msg = RecallMessage(
            sender = title,
            content = text,
            packageName = sbn.packageName,
            timestamp = System.currentTimeMillis()
        )
        db.insert(msg)
    }
}
