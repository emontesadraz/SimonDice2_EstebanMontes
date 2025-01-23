/*
    Clase que se encarga de almacenar los datos de la aplicación junto con los estados de la misma.
 */
package com.example.simondice2_estebanmontes.model

import androidx.compose.runtime.mutableStateOf

/**
 * Clase de tipo Singleton que almacena los datos de la aplicación
 */
object Datos {

    // Variable que almacena el record del usuario
    var record = 0

    // Variable que almacena la ronda actual
    var ronda = 0

    // Variable que almacena la secuencia de la máquina
    val secuenciaMaquina = mutableListOf<Int>()

    // Variable que almacena la secuencia del jugador
    val secuenciaJugador = mutableListOf<Int>()

    // Variable que almacena el color actual
    val isPrinted = mutableStateOf(false)

}
/*
    Enum class que define los colores que se van a utilizar en la aplicación.
 */
enum class estados (val start_activo: Boolean, val boton_activo: Boolean, val colorearSecuencia: Boolean) {
    ESPERANDO(start_activo = true, boton_activo = false, colorearSecuencia = false),
    GENERANDO(start_activo = false, boton_activo = true, colorearSecuencia = true),
    JUGANDO(start_activo = false, boton_activo = true, colorearSecuencia = false),
}