package com.example.pruebaaaaaa.navigation

sealed class AppScreens (val route: String){
    object FirstScreen: AppScreens("firstScreen")
    object SecondScreen: AppScreens("secondScreen")
}