package com.example.simondice2_estebanmontes.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simondice2_estebanmontes.model.Colores

@Composable
fun crearBotones() {
    Column {
        for (i in Colores.values().indices step 2) {
            Row {
                for (j in 0..1) {
                    if (i + j < Colores.values().size) {
                        val color = Colores.values()[i + j]
                        Button(
                            onClick = { /* Acción del botón */ },
                            modifier = Modifier
                                .padding(16.dp)
                                .size(100.dp)
                                .background(color.color)
                        ) {
                            // No text inside the button
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewBotones() {
    crearBotones()
}