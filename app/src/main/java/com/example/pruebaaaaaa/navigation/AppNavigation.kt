package com.example.pruebaaaaaa.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pruebaaaaaa.screens.firstScreen
import com.example.pruebaaaaaa.screens.SecondScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.FirstScreen.route
    ) {
        composable(route = AppScreens.FirstScreen.route) {
            firstScreen(navController = navController)
        }
        composable(route = AppScreens.SecondScreen.route) {
            SecondScreen(navController = navController)
        }
    }
}