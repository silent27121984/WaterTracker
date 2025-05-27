package com.example.watertracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.watertracker.ui.screens.MainScreen
import com.example.watertracker.ui.screens.SettingsScreen
import com.example.watertracker.ui.screens.StatisticsScreen
import com.example.watertracker.ui.screens.RecommendationsScreen
import com.example.watertracker.ui.viewmodel.WaterViewModel

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Settings : Screen("settings")
    object Statistics : Screen("statistics")
    object Recommendations : Screen("recommendations")
}

/**
 * Навигация приложения
 */
@Composable
fun AppNavigation(viewModel: WaterViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(
                viewModel = viewModel,
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onStatisticsClick = { navController.navigate(Screen.Statistics.route) },
                onRecommendationsClick = { navController.navigate(Screen.Recommendations.route) }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onGoalChange = { viewModel.updateDailyGoal(it) },
                currentGoal = viewModel.dailyGoal.value
            )
        }
        composable(Screen.Statistics.route) {
            StatisticsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Recommendations.route) {
            RecommendationsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
} 