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
fun PantallaHorizontalConBotones2(
    onLoroClick: () -> Unit = {},
    onPlantaClick: () -> Unit = {},
    onNaranjaClick: () -> Unit = {},
    onRanaClick: () -> Unit = {}
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
                    text = "Selecciona algo que no es ni verde ni un animal.",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.loro),
                    contentDescription = "Loro",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onLoroClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.planta),
                    contentDescription = "Planta",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onPlantaClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.naranja),
                    contentDescription = "Naranja",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onNaranjaClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.rana),
                    contentDescription = "Rana",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onRanaClick() }
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 400
)
@Composable
fun PreviewPantallaHorizontalConBotones2() {
    MaterialTheme {
        PantallaHorizontalConBotones2()
    }
}
