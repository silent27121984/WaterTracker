package com.example.watertracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.watertracker.data.WaterEntry
import com.example.watertracker.data.WaterStatistics
import com.example.watertracker.ui.viewmodel.WaterViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: WaterViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statistics by viewModel.statistics.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val dailyGoal by viewModel.dailyGoal.collectAsState()
    val entries by viewModel.getTodayEntries().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Статистика") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Выбор даты
            DateSelector(
                selectedDate = selectedDate,
                onDateSelected = { viewModel.updateSelectedDate(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Статистика
            StatisticsCard(statistics = statistics, dailyGoal = dailyGoal)

            Spacer(modifier = Modifier.height(16.dp))

            // Список записей
            EntriesList(entries = entries)
        }
    }
}

@Composable
private fun DateSelector(
    selectedDate: Date,
    onDateSelected: (Date) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            val calendar = Calendar.getInstance()
            calendar.time = selectedDate
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            onDateSelected(calendar.time)
        }) {
            Text("<")
        }

        Text(
            text = dateFormat.format(selectedDate),
            style = MaterialTheme.typography.titleMedium
        )

        IconButton(onClick = {
            val calendar = Calendar.getInstance()
            calendar.time = selectedDate
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            onDateSelected(calendar.time)
        }) {
            Text(">")
        }
    }
}

@Composable
private fun StatisticsCard(
    statistics: WaterStatistics,
    dailyGoal: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Статистика",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            StatisticRow("Всего выпито:", "${statistics.totalAmount} мл")
            StatisticRow("Среднее:", "${statistics.averageAmount} мл")
            StatisticRow("Максимум:", "${statistics.maxAmount} мл")
            StatisticRow("Минимум:", "${statistics.minAmount} мл")
            StatisticRow("Дней с данными:", statistics.daysWithData.toString())
            StatisticRow("Дней с достижением цели:", statistics.goalAchievedDays.toString())
            StatisticRow("Цель:", "$dailyGoal мл")
        }
    }
}

@Composable
private fun StatisticRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value)
    }
}

@Composable
private fun EntriesList(
    entries: List<WaterEntry>
) {
    Column {
        Text(
            text = "Записи",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(entries) { entry ->
                EntryItem(entry = entry)
            }
        }
    }
}

@Composable
private fun EntryItem(
    entry: WaterEntry
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = timeFormat.format(entry.timestamp),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = entry.type.displayName,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "${entry.amount} мл",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
} 