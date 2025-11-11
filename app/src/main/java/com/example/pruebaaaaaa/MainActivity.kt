package com.example.pruebaaaaaa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pruebaaaaaa.ui.theme.PruebaaaaaaTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class Question(
    val Pregunta: String,
    val a: String,
    val b: String,
    val c: String,
    val d: String
)

private val preguntas: List<Question> = listOf(
    Question("¿Cuánto es 1 + 2?", "1", "2", "3", "4"),
    Question("¿Cuánto es 3 × 6666?", "7", "10", "12", "14"),
    Question("¿Cuál es la ctal de Perú?", "Arequipa", "Lima", "Cusco", "Puno"),
    Question("¿Cuántos continentes hay en el mundo?", "5", "6", "7", "8"),
    Question("¿Cuál es el resultado de 9 ÷ 3?", "2", "3", "4", "5"),
    Question("¿Cuál es el océano más grande?", "Atlántico", "Índico", "Pacífico", "Ártico"),
    Question("¿Quién pintó la Mona Lisa?", "Van Gogh", "Leonardo da Vinci", "Picasso", "Rembrandt"),
    Question("¿Cuánto es 10 − 7?", "1", "2", "3", "4"),
    Question("¿Qué planeta es conocido como el planeta rojo?", "Venus", "Júpiter", "Marte", "Saturno"),
    Question("¿Cuántos lados tiene un triángulo?", "2", "3", "4", "5"),
    Question("¿Qué gas respiramos principalmente?", "Oxígeno", "Hidrógeno", "Nitrógeno", "Dióxido de carbono"),
    Question("¿Cuál es el resultado de 5 × 5?", "10", "15", "20", "25"),
    Question("¿Cuál es el mamífero más grande?", "Elefante", "Ballena azul", "Hipopótamo", "Rinoceronte"),
    Question("¿Cuántos días tiene una semana?", "5", "6", "7", "8"),
    Question("¿Qué instrumento tiene teclas blancas y negras?", "Guitarra", "Piano", "Violín", "Batería"),
    Question("¿Qué planeta está más cerca del Sol?", "Tierra", "Mercurio", "Venus", "Marte"),
    Question("¿Cuánto es 8 + 6?", "12", "13", "14", "15"),
    Question("¿Qué color se obtiene al mezclar azul y amarillo?", "Verde", "Naranja", "Rojo", "Morado"),
    Question("¿Cuál es la lengua oficial de Brasil?", "Español", "Portugués", "Francés", "Inglés"),
    Question("¿Cuántos segundos tiene un minuto?", "30", "45", "60", "90")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PruebaaaaaaTheme {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            ,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(50.dp))
                        }

                        item {
                            Image(
                                painter = painterResource(id = R.drawable.ipr_5005),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(Color.Green)
                            )


                        }
                        item{
                            MyText("Mi nombre es Franklin", Color.Green, 24)
                            MyText("y voy a aprenderrrrr", Color.Magenta, 40)
                        }

                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }

                        items(preguntas) { pregunta ->
                            cuestionario(pregunta)
                        }

                        item {
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    }

            }
        }
    }
}

@Composable
fun cuestionario(pregunta : Question) {

    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable{expanded=!expanded},
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        MyText(pregunta.Pregunta, Color.Blue, 20,
            if (expanded) Int.MAX_VALUE else 1)

        Spacer(modifier = Modifier.height(4.dp))

        MaterialTheme() {
            MyText("a) "+pregunta.a, Color.Black, 16)
            MyText("b) "+pregunta.b, Color.Black, 16)
            MyText("c) "+pregunta.c, Color.Black, 16)
            MyText("d) "+pregunta.d, Color.Black, 16)
        }

    }

    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(Color.LightGray)
        .padding(horizontal = 16.dp))
}

@Composable
fun MyText(text: String, color:Color, tam:Int, lines:Int = Int.MAX_VALUE ){
    Text(
        text,
        color=color,
        fontSize = tam.sp,
        modifier = Modifier.padding(vertical = 2.dp),
        maxLines = lines
        )
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


