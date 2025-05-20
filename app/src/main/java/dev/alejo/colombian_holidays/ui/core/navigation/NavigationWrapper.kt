package dev.alejo.colombian_holidays.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.alejo.colombian_holidays.ui.home.HomeScreen
import dev.alejo.colombian_holidays.ui.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationWrapper(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route) {
            val viewModel = koinViewModel<HomeViewModel>()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(state.isLoadingNextHoliday, state.isLoadingHolidays, state.isLoadingTodayHoliday) {
                if (!state.isLoadingNextHoliday && !state.isLoadingHolidays && !state.isLoadingTodayHoliday) {
                    viewModel.setDataLoaded()
                }
            }

            HomeScreen(
                state = state,
                onPreviousMonth = viewModel::previousMonth,
                onNextMonth = viewModel::nextMonth
            )
        }
    }
}