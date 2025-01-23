package com.example.simondice2_estebanmontes.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simondice2_estebanmontes.model.Colores

@Composable
fun UI() {
    Column {
        crearBotones()
    }
}

@Composable
fun crearBotones() {
    Column {
        Colores.values().forEach { color ->
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(16.dp)
                    .background(color.color)
            ) {
                Text("Botón ${color.numColor}")
            }
        }
    }
}

@Composable
@Preview
fun PreviewUI() {
    UI()
}