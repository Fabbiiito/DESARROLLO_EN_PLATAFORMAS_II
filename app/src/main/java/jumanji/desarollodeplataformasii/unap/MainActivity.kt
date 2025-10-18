package jumanji.desarollodeplataformasii.unap

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jumanji.desarollodeplataformasii.unap.ui.theme.JUMANJITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 🔒 Mantener orientación horizontal
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContent {
            JUMANJITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaMenuPrincipal(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaMenuPrincipal(
    modifier: Modifier = Modifier,
    onJugarClick: () -> Unit = {},
    onEstadisticasClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 🌄 Imagen de fondo
            Image(
                painter = painterResource(id = R.drawable.fondop),
                contentDescription = "Fondo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // 🎮 Contenido principal (abajo y en fila)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 50.dp), // 👈 margen inferior para subir un poco los botones
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(60.dp), // 👈 separación entre los botones
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Spacer(modifier = Modifier.width(100.dp)) // margen lateral izquierdo opcional

                    // 🕹️ Botón "Jugar"
                    Image(
                        painter = painterResource(id = R.drawable.jugar),
                        contentDescription = "Jugar",
                        modifier = Modifier
                            .size(200.dp)
                            .clickable { onJugarClick() }
                    )

                    // 📊 Botón "Estadísticas"
                    Image(
                        painter = painterResource(id = R.drawable.estadi),
                        contentDescription = "Estadísticas",
                        modifier = Modifier
                            .size(100.dp)
                            .clickable { onEstadisticasClick() }
                    )

                    Spacer(modifier = Modifier.width(100.dp)) // margen lateral derecho opcional
                }
            }

            // (Opcional) Capa transparente
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
fun PreviewPantallaMenuPrincipal() {
    MaterialTheme {
        PantallaMenuPrincipal()
    }
}
