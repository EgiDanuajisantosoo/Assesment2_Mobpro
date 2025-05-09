package com.egidanuajisantoso.assesment2_mobpro.navigation

// Navigation.kt
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.egidanuajisantoso.assesment2_mobpro.ui.screen.BarangFormScreen
import com.egidanuajisantoso.assesment2_mobpro.ui.screen.MainScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }
        composable(route = Screen.FormBaru.route) {
            BarangFormScreen(
                navController = navController,
                action = "add",
                barangId = null
            )
        }
        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("id")
            BarangFormScreen(
                navController = navController,
                action = "edit",
                barangId = id
            )
        }
    }
}