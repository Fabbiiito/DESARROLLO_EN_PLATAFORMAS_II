package jumanji.desarollodeplataformasii.unap

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jumanji.desarollodeplataformasii.unap.ui.theme.JUMANJITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 🔒 Forzar orientación horizontal
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
        // 🌄 Fondo de imagen a pantalla completa
        Image(
            painter = painterResource(id = R.drawable.fondo), // ← tu imagen fondo.png
            contentDescription = "Fondo del menú",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 📦 Contenido del menú sobre el fondo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🦁 Logo
            Image(
                painter = painterResource(id = R.drawable.logo_jumanji), // tu logo
                contentDescription = "Logo del juego",
                modifier = Modifier
                    .width(200.dp)
                    .height(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🎮 Botones con colores diferenciados
            BotonMenu("Empezar", Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(12.dp))
            BotonMenu("Opciones", Color(0xFF2196F3))
            Spacer(modifier = Modifier.height(12.dp))
            BotonMenu("Créditos", Color(0xFFFF9800))
        }
    }
}

@Composable
fun BotonMenu(texto: String, colorFondo: Color) {
    Button(
        onClick = { /* TODO: acción según botón */ },
        colors = ButtonDefaults.buttonColors(containerColor = colorFondo),
        modifier = Modifier
            .width(150.dp)
            .height(55.dp)
    ) {
        Text(texto, fontSize = 18.sp, color = Color.White)
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun MenuPrincipalPreview() {
    JUMANJITheme {
        MenuPrincipal()
    }
}
