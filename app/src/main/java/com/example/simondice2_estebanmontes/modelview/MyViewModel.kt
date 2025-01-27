package com.example.simondice2_estebanmontes.modelview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simondice2_estebanmontes.model.Datos
import com.example.simondice2_estebanmontes.model.estados
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private val _estado = MutableStateFlow(estados.ESPERANDO)
    val estado: StateFlow<estados> = _estado

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _ronda = MutableStateFlow(0)
    val ronda: StateFlow<Int> = _ronda

    private val _record = MutableStateFlow(0)
    val record: StateFlow<Int> = _record

    fun iniciarJuego() {
        viewModelScope.launch {
            Datos.reset()
            _score.value = Datos.score
            _ronda.value = Datos.ronda

            // Genera la secuencia inicial de la máquina
            crearNumerosRandom()

            // Cambia al estado COLOREANDO para mostrar la secuencia
            _estado.value = estados.COLOREANDO
        }
    }

    fun actualizarNumero(num: Int) {
        viewModelScope.launch {
            Datos.secuenciaJugador.add(num)

            if (Datos.secuenciaJugador == Datos.secuenciaMaquina.take(Datos.secuenciaJugador.size)) {
                if (Datos.secuenciaJugador.size == Datos.secuenciaMaquina.size) {
                    // Secuencia completa y correcta
                    Datos.score += 1
                    _score.value = Datos.score

                    Datos.ronda += 1
                    _ronda.value = Datos.ronda

                    if (Datos.score > Datos.record) {
                        Datos.record = Datos.score
                        _record.value = Datos.record
                    }

                    // Limpia la secuencia del jugador y añade un nuevo número a la secuencia de la máquina
                    Datos.secuenciaJugador.clear()
                    crearNumerosRandom()

                    // Cambia al estado COLOREANDO para mostrar la nueva secuencia
                    _estado.value = estados.COLOREANDO
                }
            } else {
                // Secuencia incorrecta
                _estado.value = estados.PERDIDO
            }
        }
    }

    private fun crearNumerosRandom() {
        // Añade un nuevo número aleatorio a la secuencia de la máquina
        Datos.secuenciaMaquina.add((0..3).random())
    }

    fun resetGame() {
        viewModelScope.launch {
            Datos.reset()
            _score.value = Datos.score
            _ronda.value = Datos.ronda
            _estado.value = estados.ESPERANDO
        }
    }

    fun reproducirSecuenciaMaquina(onColorChange: (Int) -> Unit) {
        viewModelScope.launch {
            // Reproduce la secuencia de la máquina paso a paso
            Datos.secuenciaMaquina.forEach { colorIndex ->
                onColorChange(colorIndex) // Cambia el color mostrado
                delay(500) // Muestra el color durante 500ms
                onColorChange(-1) // Vuelve al color predeterminado
                delay(250) // Breve pausa entre colores
            }
            _estado.value =
                estados.JUGANDO // Cambia al estado JUGANDO después de mostrar la secuencia
        }
    }
}

