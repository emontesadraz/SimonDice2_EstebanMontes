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

    // Variable que almacena la puntuación actual del usuario
    var score = 0

    // Variable que almacena la ronda actual
    var ronda = 0

    // Variable que almacena la secuencia de la máquina
    val secuenciaMaquina = mutableListOf<Int>()

    // Variable que almacena la secuencia del jugador
    val secuenciaJugador = mutableListOf<Int>()

    // Variable que almacena el color actual
    val isPrinted = mutableStateOf(false)


    /**
     * Función que se encarga de reiniciar los datos de la aplicación.
     */
    fun reset() {
        secuenciaMaquina.clear()  // Vacía la secuencia de la máquina
        secuenciaJugador.clear()  // Vacía la secuencia del jugador
        score = 0                 // Reinicia la puntuación
        ronda = 0                 // Reinicia la ronda
        // Nota: No reiniciamos el récord, ya que debería persistir
    }

}

/**
 * Clase enum que representa los diferentes estados de la aplicación.
 * @property start_activo Indica si el botón de inicio está activo en este estado.
 * @property boton_activo Indica si los botones están activos en este estado.
 * @property colorearSecuencia Indica si se debe colorear la secuencia en este estado.
 */
enum class estados (val start_activo: Boolean, val boton_activo: Boolean, val colorearSecuencia: Boolean) {
    ESPERANDO(start_activo = true, boton_activo = false, colorearSecuencia = false),
    GENERANDO(start_activo = false, boton_activo = true, colorearSecuencia = true),
    JUGANDO(start_activo = false, boton_activo = true, colorearSecuencia = false),
    COLOREANDO(start_activo = false, boton_activo = false, colorearSecuencia = true),
    PERDIDO(start_activo = true, boton_activo = false, colorearSecuencia = false),
}