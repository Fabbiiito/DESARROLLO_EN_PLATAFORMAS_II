package jumanji.desarollodeplataformasii.unap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaEstantesMatraces(
    onTablaUnoClick: () -> Unit = {},
    onTablaIgualClick: () -> Unit = {},
    onTablaDosClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFBF79)) // Fondo color #ffbf79
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFF9F37), shape = MaterialTheme.shapes.medium)
                    .padding(12.dp)
            ) {
                Text(
                    text = "¿En qué estantes hay más matraces?",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Image(
                painter = painterResource(id = R.drawable.matraces),
                contentDescription = "Matraces",
                modifier = Modifier
                    .size(250.dp)
                    .padding(top = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tablauno),
                    contentDescription = "Tabla Uno",
                    modifier = Modifier
                        .size(110.dp)
                        .clickable { onTablaUnoClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.tablaigual),
                    contentDescription = "Tabla Igual",
                    modifier = Modifier
                        .size(110.dp)
                        .clickable { onTablaIgualClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.tablados),
                    contentDescription = "Tabla Dos",
                    modifier = Modifier
                        .size(110.dp)
                        .clickable { onTablaDosClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
fun PreviewPantallaEstantesMatraces() {
    MaterialTheme {
        PantallaEstantesMatraces()
    }
}
