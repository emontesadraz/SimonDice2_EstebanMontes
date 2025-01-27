package com.example.simondice2_estebanmontes.modelview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simondice2_estebanmontes.model.Datos
import com.example.simondice2_estebanmontes.model.estados

/**
 * Clase que se encarga de almacenar los datos de la aplicación junto con los estados de la misma.
 */
class MyViewModel : ViewModel() {
    /**
     * LiveData para el estado de la aplicación
     */
    val estadoLiveData: MutableLiveData<estados> = MutableLiveData(estados.ESPERANDO)

    /**
     * LiveData para guardar el record del usuario
     */
    val record: MutableLiveData<Int> = MutableLiveData(Datos.record)

    /**
     * LiveData para guardar la puntuación del usuario
     */
    val score: MutableLiveData<Int> = MutableLiveData(Datos.score)

    /**
     * LiveData para guardar la ronda actual
     */
    val ronda: MutableLiveData<Int> = MutableLiveData(Datos.ronda)

    /**
     * Función para aumentar la ronda
     */
    private fun aumentarRonda() {
        Datos.ronda += 1
    }

    /**
     * Función para generar la secuencia de la máquina y cambiar el estado a GENERANDO
     */
    fun crearNumerosRandom() {
        Log.d("MyViewModel", "crearNumerosRandom")
        Datos.secuenciaMaquina.add((0..3).random())
        mostrarSecuencia()
    }

    /**
     * Función para actualizar la secuencia del jugador y cambiar el estado a JUGANDO
     * @param numero El número a añadir a la secuencia del jugador
     */
    fun actualizarNumero(numero: Int) {
        Datos.secuenciaJugador.add(numero)
        val index = Datos.secuenciaJugador.size - 1

        if (Datos.secuenciaJugador[index] != Datos.secuenciaMaquina[index]) {
            // Secuencia incorrecta
            finalizarJuego()
        } else if (Datos.secuenciaJugador.size == Datos.secuenciaMaquina.size) {
            // Secuencia completa y correcta
            Datos.secuenciaJugador.clear()
            crearNumerosRandom() // Genera una nueva ronda
            estadoLiveData.value = estados.COLOREANDO
        }
    }


    /**
     * Función para comprobar si la secuencia del jugador es correcta
     */
    fun mostrarSecuencia() {
        estadoLiveData.value = estados.COLOREANDO
    }

    /**
     * Función para comprobar si la secuencia del jugador es correcta
     */
    fun comprobarSecuencia() {
        if (Datos.secuenciaJugador == Datos.secuenciaMaquina) {
            aumentarRonda()
            Datos.score += 1
            Datos.actualizarRecord()
            Datos.secuenciaJugador.clear()
            crearNumerosRandom()
            estadoLiveData.value = estados.COLOREANDO
        } else {
            finalizarJuego()
        }
    }

    /**
     * Función para iniciar el juego
     */
    fun iniciarJuego() {
        Datos.score = 0
        Datos.secuenciaJugador.clear()
        Datos.secuenciaMaquina.clear()
        estadoLiveData.value = estados.GENERANDO
        crearNumerosRandom()
    }


    /**
     * Función para finalizar el juego
     */
    fun finalizarJuego(){
        Datos.score = 0
        Datos.secuenciaJugador.clear()
        Datos.secuenciaMaquina.clear()
        estadoLiveData.value = estados.ESPERANDO
    }

}