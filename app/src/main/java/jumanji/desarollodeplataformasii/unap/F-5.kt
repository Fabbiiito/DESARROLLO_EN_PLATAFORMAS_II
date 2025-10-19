package jumanji.desarollodeplataformasii.unap

import android.os.Bundle
import androidx.constraintlayout.compose.ChainStyle
import androidx.compose.foundation.border
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale

import androidx.constraintlayout.compose.ConstraintLayout

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.padding


class FigF5 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaginaFigF5()

        }
    }
}




@Preview
@Composable
fun PaginaFigF5() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()


    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )



        val (c1, c2, c3) = createRefs()
        val (casa, musica) = createRefs()
        val (cajaprincipal, texto) = createRefs()

        //  Tamaño cajitas
        val boxSize = 100.dp
        val cornerBoxSize = 100.dp

        //  Tamaño para cajaprincipal
        val principalBoxWidth = 200.dp
        val principalBoxHeight = 200.dp

        // Tamaño para texto
        val textoBoxWidth = 400.dp
        val textoBoxHeight = 50.dp

        //======================== cajas  principales =======================

        // 1. cajaprincipal
        Box(
            Modifier

                .size(principalBoxWidth, principalBoxHeight)
                .background(Color.Green.copy(alpha = 0.7f))
                .border(2.dp, Color.DarkGray)
                .constrainAs(cajaprincipal) {

                    top.linkTo(parent.top, margin = 100.dp)
                    start.linkTo(c2.start)
                    end.linkTo(c2.end)
                }
        ){
            Image(
                painter = painterResource(id = R.drawable.f_cinco_p),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // 2. Recuadro texto
        Box(
            Modifier
                .size(textoBoxWidth, textoBoxHeight)
                .constrainAs(texto) {

                    top.linkTo(parent.top, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {

            Text(
                text = "Encuentra la sombra correcta",
                color = Color.Black,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(4.dp),
                textAlign = TextAlign.Center
            )
        }

        // ================  cajas esquinas=========================

        // Recuadro casa
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 80.dp)

                .constrainAs(casa) {
                    top.linkTo(parent.top, margin = 20.dp)
                    start.linkTo(parent.start, margin = 80.dp)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.casa),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        // Recuadro musica
        Box(
            Modifier
                .size(width = 100.dp, height = 100.dp)

                .constrainAs(musica) {
                    top.linkTo(parent.top, margin = 17.dp)
                    end.linkTo(parent.end, margin = 75.dp)
                }
        ){
            Image(
                painter = painterResource(id = R.drawable.musica),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // ================= cajitas =====================

        createHorizontalChain(
            c1, c2, c3,
            chainStyle = ChainStyle.Spread
        )

        // Recuadro 1
        Box(
            Modifier
                .size(boxSize)
                .border(2.dp, Color(0xFF8B4513))
                .constrainAs(c1) {
                    top.linkTo(parent.top, margin = 300.dp)
                }
        ){
            Image(
                painter = painterResource(id = R.drawable.f_cinco_s_u),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Recuadro 2
        Box(
            Modifier
                .size(boxSize)
                .border(2.dp, Color(0xFF8B4513))
                .constrainAs(c2) {
                    // La caja principal se alinea a este
                    top.linkTo(parent.top, margin = 300.dp)
                }
        ){
            Image(
                painter = painterResource(id = R.drawable.f_cinco_s_d),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Recuadro 3
        Box(
            Modifier
                .size(boxSize)
                .border(2.dp,Color(0xFF8B4513))
                .constrainAs(c3) {
                    top.linkTo(parent.top, margin = 300.dp)
                }
        ){
            Image(
                painter = painterResource(id = R.drawable.f_cinco_s_t),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}