package com.example.watertracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Рекомендации") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
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
                .verticalScroll(rememberScrollState())
        ) {
            RecommendationCard(
                title = "Научно обоснованные рекомендации",
                content = "• Рекомендуемая норма: 2.7 литра для женщин и 3.7 литра для мужчин в день (включая воду из пищи)\n" +
                        "• Пейте воду за 30 минут до еды для улучшения пищеварения\n" +
                        "• Поддерживайте постоянный уровень гидратации в течение дня\n" +
                        "• Увеличивайте потребление воды при физической активности: +500-1000 мл на каждый час тренировки\n" +
                        "• При высоких температурах воздуха увеличьте потребление на 20-30%"
            )

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationCard(
                title = "Клинические признаки обезвоживания",
                content = "• Умеренное обезвоживание:\n" +
                        "  - Жажда\n" +
                        "  - Сухость слизистых\n" +
                        "  - Снижение диуреза\n" +
                        "  - Концентрированная моча\n" +
                        "• Тяжелое обезвоживание:\n" +
                        "  - Тахикардия\n" +
                        "  - Гипотония\n" +
                        "  - Спутанность сознания\n" +
                        "  - Олигурия"
            )

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationCard(
                title = "Показания к увеличению потребления воды",
                content = "• Физическая активность и спорт\n" +
                        "• Высокая температура окружающей среды\n" +
                        "• Повышенная температура тела\n" +
                        "• Беременность и лактация\n" +
                        "• Прием диуретиков\n" +
                        "• Диарея и рвота\n" +
                        "• Высокобелковая диета\n" +
                        "• Употребление алкоголя и кофеина"
            )

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationCard(
                title = "Физиологические функции воды",
                content = "• Терморегуляция и поддержание температуры тела\n" +
                        "• Транспорт питательных веществ и кислорода\n" +
                        "• Выведение продуктов метаболизма\n" +
                        "• Поддержание объема крови и артериального давления\n" +
                        "• Смазка суставов и защита тканей\n" +
                        "• Участие в биохимических реакциях\n" +
                        "• Поддержание электролитного баланса"
            )

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationCard(
                title = "Оптимальное время потребления воды",
                content = "• Утром натощак: 200-300 мл для активации метаболизма\n" +
                        "• За 30 минут до еды: улучшение пищеварения\n" +
                        "• Между приемами пищи: поддержание гидратации\n" +
                        "• За 2-3 часа до сна: предотвращение ночного обезвоживания\n" +
                        "• До, во время и после физической активности"
            )
        }
    }
}

@Composable
private fun RecommendationCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
} 