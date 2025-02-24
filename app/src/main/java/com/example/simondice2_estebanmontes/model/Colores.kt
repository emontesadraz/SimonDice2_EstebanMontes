/*
Aquí solamente vamos a definir los colores que se van a utilizar en la aplicación.
 */
package com.example.simondice2_estebanmontes.model

import androidx.compose.ui.graphics.Color

/**
 * Enum class que define los colores que se van a utilizar en la aplicación.
 */
enum class Colores(val numColor: Int, val color: Color, val colorPressed: Color) {
    ROJO(  0, Color.Red, Color(0xFFB71C1C)),
    VERDE(1, Color.Green, Color(0xFF1B5E20)),
    AZUL(2, Color.Blue, Color(0xFF0D47A1)),
    AMARILLO(3, Color.Yellow, Color(0xFFF57F17)),
}