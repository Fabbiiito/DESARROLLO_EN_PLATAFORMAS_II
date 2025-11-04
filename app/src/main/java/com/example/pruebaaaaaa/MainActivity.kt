package com.example.pruebaaaaaa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp



data class MyMessage(val title: String, val body:String)
private val messages: List<MyMessage> = listOf(
    MyMessage("Franklin", "Alfred"),
    MyMessage("Nelly", "Adriana")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            val ScrollState= rememberScrollState()
//            Column(
//                modifier = Modifier.verticalScroll(ScrollState)
//            ){
                LazyColumn {
                    items(messages) { message ->
                        Perfil(message)
                    }
                }
//                Spacer(modifier=Modifier.height(100.dp))
//
//                vista("Franklin")
//                vista("Adrianitaaaa")
//                vista("Nelly")
//                vista("Flaicitoooo")
//            }

        }
    }
}



@Composable
fun Perfil(message: MyMessage) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ipr_5005),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .height(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .size(90.dp)
            )
            Text(text = "Mi nombre es ${message.title}", modifier = Modifier.padding(20.dp))
            Text(text = "Mi apellido es ${message.body}")
            Spacer(modifier = Modifier.height(10.dp))

            MyText("Hola mundo", Color.Green, 20)
        }
    }
}

@Composable
fun MyText(text: String, color:Color, tam:Int ){
    Text(text, color=color, fontSize = tam.sp)
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

            Text(text="holi $a")
        }

        Text(text="textp fuera de la columna")
    }
}


