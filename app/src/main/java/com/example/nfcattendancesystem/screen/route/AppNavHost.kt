package com.example.nfcattendancesystem.screen.route

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nfcattendancesystem.screen.ClassScreen
import com.example.nfcattendancesystem.screen.HomeScreen
import com.example.nfcattendancesystem.viewmodel.BackendViewModel

enum class AppScreen() {
    Home,
    Class
}


@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: BackendViewModel,
    startDestination: String = AppScreen.Home.name
) {

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = AppScreen.Home.name) {
            HomeScreen(navController = navController, viewModel = viewModel, modifier = modifier)
        }
        composable(route = AppScreen.Class.name) {
            ClassScreen(viewModel = viewModel, modifier = modifier)
        }
    }
}
