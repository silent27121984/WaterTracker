package com.example.watertracker.data

import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
import androidx.core.content.edit

class NotificationPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, false)
        set(value) = prefs.edit { putBoolean(KEY_NOTIFICATIONS_ENABLED, value) }

    var reminderInterval: Int
        get() = prefs.getInt(KEY_REMINDER_INTERVAL, 60)
        set(value) = prefs.edit { putInt(KEY_REMINDER_INTERVAL, value) }

    var startTime: String
        get() = prefs.getString(KEY_START_TIME, "09:00") ?: "09:00"
        set(value) = prefs.edit { putString(KEY_START_TIME, value) }

    var endTime: String
        get() = prefs.getString(KEY_END_TIME, "21:00") ?: "21:00"
        set(value) = prefs.edit { putString(KEY_END_TIME, value) }

    var notificationSound: String
        get() = prefs.getString(KEY_NOTIFICATION_SOUND, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()) ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
        set(value) = prefs.edit { putString(KEY_NOTIFICATION_SOUND, value) }

    var vibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION_ENABLED, true)
        set(value) = prefs.edit { putBoolean(KEY_VIBRATION_ENABLED, value) }

    companion object {
        private const val PREFS_NAME = "notification_preferences"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_REMINDER_INTERVAL = "reminder_interval"
        private const val KEY_START_TIME = "start_time"
        private const val KEY_END_TIME = "end_time"
        private const val KEY_NOTIFICATION_SOUND = "notification_sound"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
    }
} 