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
     * Inicialización de la secuencia de la máquina
     */
    init {
        inicializarSecuenciaMaquina()
    }

    /**
     * Función para inicializar la secuencia de la máquina
     */
    private fun inicializarSecuenciaMaquina() {
        Datos.secuenciaMaquina.clear()
        crearNumerosRandom()
    }

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
        estadoLiveData.value = estados.GENERANDO

    }

    /**
     * Función para actualizar la secuencia del jugador y cambiar el estado a JUGANDO
     * @param numero El número a añadir a la secuencia del jugador
     */
    fun actualizarNumero(numero: Int) {
        Log.d("MyViewModel", "actualizarNumero")
        Datos.secuenciaJugador.add(numero)
        estadoLiveData.value = estados.JUGANDO
    }

    /**
     * Función para comprobar si la secuencia del jugador es correcta
     */
    fun comprobarSecuencia() {
        Log.d("MyViewModel", "comprobarSecuencia")
        if (Datos.secuenciaJugador == Datos.secuenciaMaquina) {
            Log.d("MyViewModel", "secuencia correcta")
            aumentarRonda()
            Datos.score += 1
            Datos.actualizarRecord()
            Datos.secuenciaJugador.clear()
            crearNumerosRandom()
            estadoLiveData.value = estados.ESPERANDO
        } else {
            Log.d("MyViewModel", "secuencia incorrecta")
            Datos.score = 0
            Datos.secuenciaJugador.clear()
            Datos.secuenciaMaquina.clear()
            estadoLiveData.value = estados.ESPERANDO
        }
    }
}