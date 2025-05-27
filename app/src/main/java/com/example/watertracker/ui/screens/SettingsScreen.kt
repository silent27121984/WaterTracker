package com.example.watertracker.ui.screens

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.*
import com.example.watertracker.data.NotificationPreferences
import com.example.watertracker.notifications.WaterReminderWorker
import java.util.concurrent.TimeUnit
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onGoalChange: (Int) -> Unit,
    currentGoal: Int
) {
    val context = LocalContext.current
    val notificationPreferences = remember { NotificationPreferences(context) }

    var goal by remember { mutableStateOf(currentGoal.toString()) }
    var notificationsEnabled by remember { mutableStateOf(notificationPreferences.notificationsEnabled) }
    var reminderInterval by remember { mutableStateOf(notificationPreferences.reminderInterval.toString()) }
    var startTime by remember { mutableStateOf(notificationPreferences.startTime) }
    var endTime by remember { mutableStateOf(notificationPreferences.endTime) }
    var notificationSound by remember { mutableStateOf(notificationPreferences.notificationSound) }

    val ringtonePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getParcelableExtra<android.net.Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)?.let { uri ->
                notificationSound = uri.toString()
                notificationPreferences.notificationSound = uri.toString()
                if (notificationsEnabled) {
                    scheduleReminders(
                        reminderInterval.toIntOrNull() ?: 60,
                        startTime,
                        endTime,
                        uri.toString()
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Суточная норма
            OutlinedTextField(
                value = goal,
                onValueChange = { 
                    goal = it.filter { char -> char.isDigit() }
                    goal.toIntOrNull()?.let { value -> onGoalChange(value) }
                },
                label = { Text("Суточная норма (мл)") },
                modifier = Modifier.fillMaxWidth()
            )

            Divider()

            // Настройки уведомлений
            Text(
                text = "Уведомления",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Включить напоминания")
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { enabled ->
                        notificationsEnabled = enabled
                        notificationPreferences.notificationsEnabled = enabled
                        if (enabled) {
                            scheduleReminders(
                                reminderInterval.toIntOrNull() ?: 60,
                                startTime,
                                endTime,
                                notificationSound
                            )
                        } else {
                            cancelReminders()
                        }
                    }
                )
            }

            OutlinedTextField(
                value = reminderInterval,
                onValueChange = { 
                    reminderInterval = it.filter { char -> char.isDigit() }
                    reminderInterval.toIntOrNull()?.let { value ->
                        notificationPreferences.reminderInterval = value
                        if (notificationsEnabled) {
                            scheduleReminders(
                                value,
                                startTime,
                                endTime,
                                notificationSound
                            )
                        }
                    }
                },
                label = { Text("Интервал напоминаний (минуты)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = notificationsEnabled
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { 
                        if (isValidTimeFormat(it)) {
                            startTime = it
                            notificationPreferences.startTime = it
                            if (notificationsEnabled) {
                                scheduleReminders(
                                    reminderInterval.toIntOrNull() ?: 60,
                                    startTime,
                                    endTime,
                                    notificationSound
                                )
                            }
                        }
                    },
                    label = { Text("Время начала") },
                    modifier = Modifier.weight(1f),
                    enabled = notificationsEnabled
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { 
                        if (isValidTimeFormat(it)) {
                            endTime = it
                            notificationPreferences.endTime = it
                            if (notificationsEnabled) {
                                scheduleReminders(
                                    reminderInterval.toIntOrNull() ?: 60,
                                    startTime,
                                    endTime,
                                    notificationSound
                                )
                            }
                        }
                    },
                    label = { Text("Время окончания") },
                    modifier = Modifier.weight(1f),
                    enabled = notificationsEnabled
                )
            }

            Button(
                onClick = {
                    val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
                        putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
                        putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Выберите звук уведомления")
                        putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, android.net.Uri.parse(notificationSound))
                        putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
                        putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                    }
                    ringtonePickerLauncher.launch(intent)
                },
                enabled = notificationsEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Выбрать звук уведомления")
            }
        }
    }
}

private fun isValidTimeFormat(time: String): Boolean {
    return try {
        LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        true
    } catch (e: Exception) {
        false
    }
}

private fun scheduleReminders(
    intervalMinutes: Int,
    startTime: String,
    endTime: String,
    soundUri: String
) {
    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .build()

    val reminderRequest = PeriodicWorkRequestBuilder<WaterReminderWorker>(
        intervalMinutes.toLong(),
        TimeUnit.MINUTES
    )
        .setConstraints(constraints)
        .setInputData(workDataOf(
            "startTime" to startTime,
            "endTime" to endTime,
            "soundUri" to soundUri
        ))
        .build()

    WorkManager.getInstance().enqueueUniquePeriodicWork(
        "water_reminder",
        ExistingPeriodicWorkPolicy.UPDATE,
        reminderRequest
    )
}

private fun cancelReminders() {
    WorkManager.getInstance().cancelUniqueWork("water_reminder")
} 