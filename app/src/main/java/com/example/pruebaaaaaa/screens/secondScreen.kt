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
import androidx.compose.foundation.layout.padding


@Composable
fun SecondScreen(){
    Scaffold{ paddingValues ->
        SecondBodyContent(paddingValues)
    }
}

@Composable
fun SecondBodyContent(paddingValues: PaddingValues){
    Column (
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Hola navegacion")
        Button(onClick={/*TODO*/}){
            Text("navega")
        }
    }
}



