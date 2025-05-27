package com.example.watertracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.watertracker.data.DrinkType
import com.example.watertracker.ui.viewmodel.WaterViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: WaterViewModel,
    onSettingsClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onRecommendationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val todayTotal by viewModel.getTodayTotal().collectAsState(initial = 0)
    val dailyGoal by viewModel.dailyGoal.collectAsState(initial = 2000)
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedAmount by remember { mutableStateOf(200) }
    var selectedType by remember { mutableStateOf(DrinkType.WATER) }

    val progress = if (dailyGoal > 0) (todayTotal.toFloat() / dailyGoal) else 0f
    val percentage = if (dailyGoal > 0) (todayTotal.toFloat() / dailyGoal * 100).toInt() else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Трекер воды") },
                actions = {
                    IconButton(onClick = onRecommendationsClick) {
                        Icon(Icons.Default.Info, contentDescription = "Рекомендации")
                    }
                    IconButton(onClick = onStatisticsClick) {
                        Icon(Icons.Default.BarChart, contentDescription = "Статистика")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(200.dp),
                strokeWidth = 12.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "$todayTotal мл / $dailyGoal мл",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickAddButton(amount = 100) { viewModel.addWaterEntry(it) }
                QuickAddButton(amount = 200) { viewModel.addWaterEntry(it) }
                QuickAddButton(amount = 300) { viewModel.addWaterEntry(it) }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Добавить воду") },
                text = {
                    Column {
                        Text("Тип напитка:")
                        DrinkTypeSelector(
                            selectedType = selectedType,
                            onTypeSelected = { selectedType = it }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Количество (мл):")
                        Slider(
                            value = selectedAmount.toFloat(),
                            onValueChange = { selectedAmount = it.toInt() },
                            valueRange = 50f..1000f,
                            steps = 19
                        )
                        Text("$selectedAmount мл")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.addWaterEntry(selectedAmount, selectedType)
                            showAddDialog = false
                        }
                    ) {
                        Text("Добавить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}

@Composable
fun QuickAddButton(
    amount: Int,
    onClick: (Int) -> Unit
) {
    Button(
        onClick = { onClick(amount) },
        modifier = Modifier
            .padding(4.dp)
            .width(100.dp)
    ) {
        Text("+$amount мл")
    }
}

@Composable
private fun DrinkTypeSelector(
    selectedType: DrinkType,
    onTypeSelected: (DrinkType) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DrinkType.values().forEach { type ->
            FilterChip(
                selected = type == selectedType,
                onClick = { onTypeSelected(type) },
                label = { Text(type.displayName) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
} 