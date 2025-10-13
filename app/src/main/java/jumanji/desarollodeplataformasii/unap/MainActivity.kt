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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jumanji.desarollodeplataformasii.unap.ui.theme.JUMANJITheme

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

            BotonMenu("Empezar", Color(0xFF4CAF50)) { /* Aquí puedes navegar a la nueva vista */ }
            Spacer(modifier = Modifier.height(12.dp))
            BotonMenu("Opciones", Color(0xFF2196F3)) { /* Otra acción */ }
            Spacer(modifier = Modifier.height(12.dp))
            BotonMenu("Créditos", Color(0xFFFF9800)) { /* Otra acción */ }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Nueva sección para mostrar imagen por número
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
/*
primera idea de juego dato curioso: se trata de que el usuario ingrese un numero y despues de presionar 
el boton mostrar se mostrara una imagen con un dato curioso  o algo importante sobre este numero.
*/
@Composable
fun MostrarImagenPorNumero() {
    var numero by remember { mutableStateOf("") }
    var imagenRes by remember { mutableStateOf<Int?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        // ✅ Título / instrucción
        Text(
            text = "Bienvenido a dato curioso, escribe un número y te diremos algo que no conocías",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        OutlinedTextField(
            value = numero,
            onValueChange = { input ->
                // Permitimos solo números del 1 al 30
                if (input.isEmpty() || (input.toIntOrNull() in 1..30)) {
                    numero = input
                }
            },
            label = { Text("Ingresa un número (1-30)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(300.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            // Seleccionamos el drawable correspondiente
            imagenRes = when (numero.toIntOrNull()) {
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
                else -> null
            }
        }) {
            Text("Mostrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        imagenRes?.let { res ->
            Image(
                painter = painterResource(id = res),
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )
        }
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
