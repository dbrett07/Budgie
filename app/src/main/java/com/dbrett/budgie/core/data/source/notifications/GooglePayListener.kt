package com.dbrett.budgie.core.data.source.notifications

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

import java.util.concurrent.ConcurrentHashMap

import com.dbrett.budgie.core.data.source.local.LocalTransactionDataSourceImpl
import com.dbrett.budgie.core.data.source.local.entity.TransactionEntity

class GooglePayListener : NotificationListenerService()  {
    lateinit var localDataSource : LocalTransactionDataSourceImpl
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val GPAYGLOBAL = "com.google.android.apps.walletnfcrel"
    private val GPAYINDIA = "com.google.android.apps.nbu.paisa.user"

    // map notification id to post time so we don't get any duplicates committed into the db
    private val processedNotifications = ConcurrentHashMap<String, Long>()

    private fun cleanCache()  {
        val now = System.currentTimeMillis()
        processedNotifications.entries.removeIf {now - it.value > 300_000}
    }

    override fun onNotificationPosted(statusBarNotification: StatusBarNotification?) {
        if (statusBarNotification == null) return

        val `package` = statusBarNotification.packageName
        if (`package` != GPAYGLOBAL && `package` != GPAYINDIA) {
            return
        }

        val notificationKey = statusBarNotification.key
        val postTime = statusBarNotification.postTime
        val title = statusBarNotification.notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString()

        if (processedNotifications.containsKey(notificationKey)) return
        processedNotifications[notificationKey] = postTime
        val extras = statusBarNotification.notification.extras ?: return

        var rawPayload = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString().orEmpty()
        if (rawPayload.isEmpty()) {
            rawPayload = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString().orEmpty()
        }

        if (rawPayload.isEmpty()) {
            val lines = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)
            if (!lines.isNullOrEmpty()) {
                rawPayload = lines.last().toString()
            }
        }

        if (rawPayload.isBlank()) return
        val transInfo : TransactionEntity = NotificationParser.parse(postTime, title, rawPayload)

        serviceScope.launch {
            localDataSource.saveTransaction(transInfo)
        }

        cleanCache()
    }
}