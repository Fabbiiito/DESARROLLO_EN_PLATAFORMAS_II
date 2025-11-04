package com.example.pruebaaaaaa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pruebaaaaaa.ui.theme.PruebaaaaaaTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(){
                Spacer(modifier=Modifier.height(100.dp))
                Perfil("Franklin", "Alfred")
            }

        }
    }
}

@Composable
fun Perfil(name:String, lastname:String){
    MaterialTheme {
        Column(
            modifier=Modifier.padding(10.dp).fillMaxWidth(),
            horizontalAlignment=Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ipr_5005),
                contentDescription = "Frank",
                modifier=Modifier.height(100.dp)
            )
            Text(text="Mi nombre es $name", modifier=Modifier.padding(20.dp))
            Text(text="Mi apellido es $lastname")

        }
    }
}












@Composable
private fun vista(a:String){
    MaterialTheme {
        Column(
            modifier=Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Image(
                painter = painterResource(R.drawable.ipr_5005),
                contentDescription = "tengo tu foto",
                modifier=Modifier.height(100.dp)
            )
            Text(text="prueba xddd, de cmna")

            Text(text="holi $a")
        }

        Text(text="textp fuera de la columna")
    }
}
