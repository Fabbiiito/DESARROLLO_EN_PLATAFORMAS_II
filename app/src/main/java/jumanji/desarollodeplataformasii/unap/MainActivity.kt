package jumanji.desarollodeplataformasii.unap

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jumanji.desarollodeplataformasii.unap.ui.theme.JUMANJITheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContent {
            JUMANJITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MenuPrincipal(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuPrincipal(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo del menú",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_jumanji),
                contentDescription = "Logo del juego",
                modifier = Modifier
                    .width(200.dp)
                    .height(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            BotonMenu("Empezar", Color(0xFF4CAF50)) { /* Navegar a juego */ }
            Spacer(modifier = Modifier.height(12.dp))
            BotonMenu("Opciones", Color(0xFF2196F3)) { /* Navegar a opciones */ }
            Spacer(modifier = Modifier.height(12.dp))
            BotonMenu("Créditos", Color(0xFFFF9800)) { /* Navegar a créditos */ }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de datos curiosos
            MostrarImagenPorNumero()
        }
    }
}

@Composable
fun BotonMenu(texto: String, colorFondo: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = colorFondo),
        modifier = Modifier
            .width(150.dp)
            .height(55.dp)
    ) {
        Text(texto, fontSize = 18.sp, color = Color.White)
    }
}

@Composable
fun MostrarImagenPorNumero() {
    var numero by remember { mutableStateOf("") }
    var imagenRes by remember { mutableIntStateOf(0) }
    var mostrarImagen by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        // Título
        Text(
            text = "Bienvenido a dato curioso, escribe un número y te diremos algo que no conocías",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = numero,
            onValueChange = { input ->
                // Permitir solo números del 1 al 30
                if (input.isEmpty() || input.all { it.isDigit() }) {
                    val num = input.toIntOrNull()
                    if (num == null || num in 1..30) {
                        numero = input
                    }
                }
            },
            label = { Text("Ingresa un número (1-30)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.width(300.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            val num = numero.toIntOrNull()
            if (num != null && num in 1..30) {
                imagenRes = obtenerImagenPorNumero(num)
                mostrarImagen = true
            } else {
                mostrarImagen = false
            }
        }) {
            Text("Mostrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mostrarImagen && imagenRes != 0) {
            Image(
                painter = painterResource(id = imagenRes),
                contentDescription = "Imagen seleccionada: número $numero",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

// Función auxiliar para mapear números a recursos
private fun obtenerImagenPorNumero(numero: Int): Int {
    return when (numero) {
        1 -> R.drawable.imagen1
        2 -> R.drawable.imagen2
        3 -> R.drawable.imagen3
        4 -> R.drawable.imagen4
        5 -> R.drawable.imagen5
        6 -> R.drawable.imagen6
        7 -> R.drawable.imagen7
        8 -> R.drawable.imagen8
        9 -> R.drawable.imagen9
        10 -> R.drawable.imagen10
        11 -> R.drawable.imagen11
        12 -> R.drawable.imagen12
        13 -> R.drawable.imagen13
        14 -> R.drawable.imagen14
        15 -> R.drawable.imagen15
        16 -> R.drawable.imagen16
        17 -> R.drawable.imagen17
        18 -> R.drawable.imagen18
        19 -> R.drawable.imagen19
        20 -> R.drawable.imagen20
        21 -> R.drawable.imagen21
        22 -> R.drawable.imagen22
        23 -> R.drawable.imagen23
        24 -> R.drawable.imagen24
        25 -> R.drawable.imagen25
        26 -> R.drawable.imagen26
        27 -> R.drawable.imagen27
        28 -> R.drawable.imagen28
        29 -> R.drawable.imagen29
        30 -> R.drawable.imagen30
        else -> 0
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun MenuPrincipalPreview() {
    JUMANJITheme {
        MenuPrincipal()
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun MostrarImagenPorNumeroPreview() {
    JUMANJITheme {
        MostrarImagenPorNumero()
    }
}

@Composable
fun JuegoAdivinaNumero() {
    var numeroUsuario by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }
    var numeroSecreto by remember { mutableIntStateOf((1..10).random()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎯 Adivina el número",
                fontSize = 24.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = numeroUsuario,
                onValueChange = { input ->
                    if (input.isEmpty() || input.all { it.isDigit() }) {
                        numeroUsuario = input
                    }
                },
                label = { Text("Escribe un número del 1 al 10") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.width(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val num = numeroUsuario.toIntOrNull()
                    resultado = when {
                        num == null -> "Por favor ingresa un número válido."
                        num !in 1..10 -> "El número debe estar entre 1 y 10."
                        num == numeroSecreto -> {
                            val mensaje = "🎉 ¡Correcto! El número era $numeroSecreto"
                            numeroSecreto = (1..10).random()
                            numeroUsuario = ""
                            mensaje
                        }
                        num < numeroSecreto -> "🔼 El número secreto es mayor."
                        else -> "🔽 El número secreto es menor."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Comprobar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = resultado,
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun JuegoAdivinaNumeroPreview() {
    JUMANJITheme {
        JuegoAdivinaNumero()
    }
}

@Composable
fun RuletaDeLaSuerte() {
    var resultado by remember { mutableStateOf("") }
    var girando by remember { mutableStateOf(false) }

    val opciones = listOf(
        "🎁 ¡Ganaste un premio sorpresa!",
        "🍀 Hoy tendrás buena suerte.",
        "💸 Doble de puntos en tu próximo turno.",
        "🦸‍♂️ Poder especial desbloqueado.",
        "😅 Intenta de nuevo...",
        "🎉 ¡Felicidades, eres el ganador del día!",
        "🌟 Premio misterioso oculto."
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎡 Ruleta de la Suerte",
                fontSize = 28.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.ruleta),
                contentDescription = "Ruleta",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (!girando) {
                        girando = true
                        resultado = "Girando..."
                    }
                },
                enabled = !girando,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Text(
                    text = if (girando) "Girando..." else "🎯 Girar",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            // Efecto para simular el giro
            LaunchedEffect(girando) {
                if (girando) {
                    delay(2000L)
                    resultado = opciones.random()
                    girando = false
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = resultado,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun RuletaDeLaSuertePreview() {
    JUMANJITheme {
        RuletaDeLaSuerte()
    }
}
