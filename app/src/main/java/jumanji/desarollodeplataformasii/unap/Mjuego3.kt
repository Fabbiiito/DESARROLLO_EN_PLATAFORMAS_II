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
fun PantallaSeleccionPieza(
    onCor1Click: () -> Unit = {},
    onCor2Click: () -> Unit = {},
    onCor3Click: () -> Unit = {},
    onCor4Click: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFBF79)) // fondo color #ffbf79
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
                    text = "Selecciona la pieza correcta.",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Image(
                painter = painterResource(id = R.drawable.corona),
                contentDescription = "Corona",
                modifier = Modifier
                    .size(220.dp)
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
                    painter = painterResource(id = R.drawable.cor1),
                    contentDescription = "Opción 1",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onCor1Click() }
                )
                Image(
                    painter = painterResource(id = R.drawable.cor2),
                    contentDescription = "Opción 2",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onCor2Click() }
                )
                Image(
                    painter = painterResource(id = R.drawable.cor3),
                    contentDescription = "Opción 3",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onCor3Click() }
                )
                Image(
                    painter = painterResource(id = R.drawable.cor4),
                    contentDescription = "Opción 4",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onCor4Click() }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
fun PreviewPantallaSeleccionPieza() {
    MaterialTheme {
        PantallaSeleccionPieza()
    }
}
