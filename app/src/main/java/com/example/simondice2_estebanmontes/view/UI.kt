package com.example.simondice2_estebanmontes.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.simondice2_estebanmontes.model.Colores
import com.example.simondice2_estebanmontes.model.Datos
import com.example.simondice2_estebanmontes.model.estados
import com.example.simondice2_estebanmontes.modelview.MyViewModel

/**
 * Funcion principal de la aplicacion
 */
@Composable
fun App(viewModel: MyViewModel, paddingValues: PaddingValues) {
    val estado by viewModel.estadoLiveData.observeAsState(estados.ESPERANDO)
    val secuenciaMaquina = Datos.secuenciaMaquina
    var currentColorIndex by remember { mutableStateOf(-1) }

    LaunchedEffect(estado) {
        if (estado == estados.COLOREANDO) {
            for (index in secuenciaMaquina.indices) {
                currentColorIndex = secuenciaMaquina[index]
                delay(500) // Muestra cada color durante 500ms
                currentColorIndex = -1 // Pausa entre colores
                delay(250)
            }
            viewModel.estadoLiveData.value = estados.JUGANDO // Pasamos al estado JUGANDO
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            textoRecord(viewModel)
            textoPuntuacion(viewModel)
            textoRonda(viewModel)
            if (estado == estados.ESPERANDO) {
                BotonStart(viewModel)
            } else {
                botonColores(viewModel, currentColorIndex)
            }
        }
    }
}


@Composable
fun botonColores(viewModel: MyViewModel, currentColorIndex: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for (i in 0..1) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                for (j in 0..1) {
                    val color = Colores.values()[i * 2 + j]
                    val isHighlighted = currentColorIndex == color.numColor // Compara con el índice actual

                    Button(
                        onClick = { viewModel.actualizarNumero(color.numColor) },
                        modifier = Modifier
                            .padding(16.dp)
                            .size(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isHighlighted) color.color.copy(alpha = 0.5f) else color.color
                        ),
                        shape = RectangleShape
                    ) {}
                }
            }
        }
    }
}

@Composable
fun BotonStart(viewModel: MyViewModel) {
    var colorIndex by remember { mutableStateOf(0) }
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)

    LaunchedEffect(Unit) {
        while (true) {
            delay(500) // Cambia el color cada 500ms
            colorIndex = (colorIndex + 1) % colors.size
        }
    }

    Button(
        onClick = { viewModel.iniciarJuego() },
        modifier = Modifier.padding(28.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colors[colorIndex])
    ) {
        Text(text = "Start")
    }
}

/**
 * Funcion para mostrar el texto de la puntuacion
 * @param viewModel instancia de MyViewModel
 */
@Composable
fun textoPuntuacion(viewModel: MyViewModel){
    val puntuacion by viewModel.score.observeAsState()
    Text(
        text = "Puntuacion: $puntuacion",
        fontSize = 20.sp
    )
}

/**
 * Funcion para mostrar el texto de la ronda
 * @param viewModel instancia de MyViewModel
 */
@Composable
fun textoRonda(viewModel: MyViewModel){
    val ronda by viewModel.ronda.observeAsState()
    Text(
        text = "Ronda: $ronda",
        fontSize = 20.sp
    )
}

@Composable
fun textoRecord(viewModel: MyViewModel){
    val record by viewModel.record.observeAsState()
    Text(
        text = "Record: $record",
        fontSize = 20.sp
    )
}


@Composable
@Preview
fun PreviewApp() {
    App(MyViewModel(), PaddingValues(16.dp))
}