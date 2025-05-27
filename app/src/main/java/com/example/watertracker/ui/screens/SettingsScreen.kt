package com.example.watertracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.watertracker.ui.viewmodel.WaterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: WaterViewModel = viewModel()
) {
    var dailyGoal by remember { mutableStateOf(viewModel.dailyGoal.value) }

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
                .padding(16.dp)
        ) {
            // Настройка дневной цели
            Text(
                text = "Дневная цель",
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Установите количество воды, которое вы хотите выпивать каждый день",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Слайдер для выбора цели
            Slider(
                value = dailyGoal.toFloat(),
                onValueChange = { dailyGoal = it.toInt() },
                valueRange = 500f..5000f,
                steps = 45,
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(
                text = "$dailyGoal мл",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Кнопка сохранения
            TextButton(
                onClick = {
                    viewModel.updateDailyGoal(dailyGoal)
                    onNavigateBack()
                }
            ) {
                Text("Сохранить")
            }
        }
    }
} 