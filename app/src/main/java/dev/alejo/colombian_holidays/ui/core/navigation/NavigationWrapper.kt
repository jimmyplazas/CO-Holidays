package dev.alejo.colombian_holidays.ui.core.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.alejo.colombian_holidays.domain.model.PublicHolidayModel
import dev.alejo.colombian_holidays.ui.detail.DetailScreen
import dev.alejo.colombian_holidays.ui.detail.DetailViewModel
import dev.alejo.colombian_holidays.ui.home.HomeScreen
import dev.alejo.colombian_holidays.ui.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavigationWrapper(navController: NavHostController) {
    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = Routes.Home) {
            composable<Routes.Home> {
                val viewModel = koinViewModel<HomeViewModel>()
                val state by viewModel.state.collectAsState()
                val events by viewModel.events.collectAsState()

                HomeScreen(
                    state = state,
                    events = events,
                    animatedVisibilityScope = this,
                    onViewLayoutClick = viewModel::onViewLayoutClick,
                    onHolidaysAction = viewModel::onHolidaysAction,
                    onHolidaySelected = { holiday ->
                        navController.navigate(
                            Routes.Detail(
                                date = holiday.date.toString(),
                                name = holiday.name,
                                localName = holiday.localName,
                                global = holiday.global,
                                launchYear = holiday.launchYear,
                                types = holiday.types
                            )
                        )
                    }
                )
            }
            composable<Routes.Detail> {
                val args = it.toRoute<Routes.Detail>()
                val holiday = PublicHolidayModel(
                    date = LocalDate.parse(args.date),
                    name = args.name,
                    localName = args.localName,
                    global = args.global,
                    launchYear = args.launchYear,
                    types = args.types
                )

                val viewModel = koinViewModel<DetailViewModel>()
                val state by viewModel.state.collectAsState()

                viewModel.initData(holiday = holiday)

                DetailScreen(
                    state = state,
                    animatedVisibilityScope = this,
                    onTurnOnNotification = viewModel::turnOnNotification,
                    onTurnOffNotification = viewModel::turnOffNotification,
                    onBack = { navController.popBackStack() },
                    onNotificationDenied = viewModel::onNotificationDenied,
                    onNotificationAllowed = viewModel::onNotificationAllowed
                )
            }
        }
    }
}