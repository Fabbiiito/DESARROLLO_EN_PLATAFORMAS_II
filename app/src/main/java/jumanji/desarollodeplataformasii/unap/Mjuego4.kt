package jumanji.desarrollodeplataformasii.unap
import jumanji.desarollodeplataformasii.unap.R
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
fun PantallaCaminoCorrecto(
    onUnoClick: () -> Unit = {},
    onDosClick: () -> Unit = {},
    onTresClick: () -> Unit = {},
    onCuatroClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFBF79))
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
                    text = "¿Qué camino es el correcto?",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Image(
                painter = painterResource(id = R.drawable.caminos),
                contentDescription = "Caminos",
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
                    painter = painterResource(id = R.drawable.uno),
                    contentDescription = "Camino 1",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onUnoClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.dos),
                    contentDescription = "Camino 2",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onDosClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.tres),
                    contentDescription = "Camino 3",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onTresClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.cuatro),
                    contentDescription = "Camino 4",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onCuatroClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
fun PreviewPantallaCaminoCorrecto() {
    MaterialTheme {
        PantallaCaminoCorrecto()
    }
}
