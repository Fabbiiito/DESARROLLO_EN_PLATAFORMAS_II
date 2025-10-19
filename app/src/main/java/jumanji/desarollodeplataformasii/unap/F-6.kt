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
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image


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


class FigF6 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaginaFigF6()
        }
    }
}

@Preview
@Composable
fun PaginaFigF6() {
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


        val (c1, c2, c3, c4) = createRefs()
        val (casa, musica) = createRefs()
        val (texto) = createRefs()

        val boxSize = 100.dp
        val textoBoxWidth = 400.dp
        val textoBoxHeight = 100.dp

        //======================== Texto  =======================
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
                text = "Selecciona algo rojo, pero no la vajilla",
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



        // Casa
        Box(
            modifier = Modifier
                .size(80.dp)
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

        // Música
        Box(
            Modifier
                .size(100.dp)
                .constrainAs(musica) {
                    top.linkTo(parent.top, margin = 17.dp)
                    end.linkTo(parent.end, margin = 75.dp)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.musica),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        //========================cajitas =======================

        createHorizontalChain(c1, c2, c3, c4, chainStyle = ChainStyle.Spread)

        // Recuadro 1
        Box(
            Modifier
                .size(boxSize)
                .border(2.dp, Color(0xFF8B4513))
                .constrainAs(c1) {
                    top.linkTo(parent.top, margin = 200.dp)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.f_seis_s_u),
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
                    top.linkTo(parent.top, margin = 200.dp)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.f_seis_s_d),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Recuadro 3
        Box(
            Modifier
                .size(boxSize)
                .border(2.dp, Color(0xFF8B4513))
                .constrainAs(c3) {
                    top.linkTo(parent.top, margin = 200.dp)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.f_seis_s_t),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        //  Recuadro 4
        Box(
            Modifier
                .size(boxSize)
                .border(2.dp, Color(0xFF8B4513))
                .constrainAs(c4) {
                    top.linkTo(parent.top, margin = 200.dp)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.f_seis_s_c),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
