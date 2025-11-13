package com.example.pruebaaaaaa.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment

import androidx.navigation.NavController
import com.example.pruebaaaaaa.navigation.AppScreens

@Composable
fun firstScreen(navController: NavController){
    Scaffold{ paddingValues ->
        SecondBodyContent(navController, paddingValues)
    }
}

@Composable
fun BodyContent(navController: NavController, paddingValues: PaddingValues){
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Hola navegacion")
        Button(onClick= {
            navController.navigate(route = AppScreens.SecondScreen.route)
        }){
            Text("navega")
        }
    }
}



