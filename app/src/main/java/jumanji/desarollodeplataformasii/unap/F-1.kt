package jumanji.desarollodeplataformasii.unap

import android.os.Bundle
import androidx.constraintlayout.compose.ChainStyle
import androidx.compose.foundation.border
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme


import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

class FigF1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                TresCuadros()

            }
        }
    }
}


@Preview
@Composable
fun TresCuadros() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

    ) {
        val (c1, c2, c3) = createRefs()
        val boxSize = 80.dp


        createHorizontalChain(
            c1, c2, c3,
            chainStyle = ChainStyle.Spread
        )

        // Recuadro 1
        Box(
            Modifier
                .size(boxSize)

                .border(2.dp, Color.Black)
                .constrainAs(c1) {
                    top.linkTo(parent.top, margin = 300.dp)
                }
        )

        // Recuadro 2
        Box(
            Modifier
                .size(boxSize)

                .border(2.dp, Color.Black)
                .constrainAs(c2) {
                    top.linkTo(parent.top, margin = 300.dp)
                }
        )

        // Recuadro 3
        Box(
            Modifier
                .size(boxSize)

                .border(2.dp, Color.Black)
                .constrainAs(c3) {
                    top.linkTo(parent.top, margin = 300.dp)
                }
        )
    }
}
