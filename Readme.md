# SIMÓN DICE
*Esteban Miguel Montes Adraz* - *2 DAM* - *PMDM*

## DESCRIPCIÓN

Simón Dice es un juego de memoria en el que los jugadores deben repetir una secuencia de colores generada aleatoriamente por el juego. La secuencia comienza con un solo color y se incrementa en longitud con cada ronda. Los jugadores deben observar la secuencia y luego reproducirla en el mismo orden. Si el jugador comete un error, el juego termina. El objetivo es recordar y reproducir la secuencia más larga posible.

El juego es una excelente manera de mejorar la memoria y la concentración, y puede ser jugado tanto por niños como por adultos. Además, puede ser disfrutado en solitario o en grupo, compitiendo para ver quién puede recordar la secuencia más larga.

## FUNCIONAMIENTO
Este proyecto está hecho en Android Studio con el lenguaje ```Kotlin```. Utiliza un patrón MVVM (Model View ViewModel)
![Imagen1](img/Captura%20de%20pantalla%202025-01-28%20a%20las%201.44.51.png) Este estilo de arquitectura hace que se vea más ordenado y mucho más cómodo a la hora de manipular el código.
Para explicarlo más en detalle:

**¿De qué se trata el modelo MVVM?**

El modelo MVVM (Model-View-ViewModel) es un patrón de arquitectura de software que se utiliza para organizar y estructurar el código, principalmente en aplicaciones con interfaces de usuario. Separa claramente la lógica de negocio y los datos de la presentación, facilitando el mantenimiento y la escalabilidad. Consta de tres partes:
1. ***Model***:
Representa la lógica de negocio y los datos puros de la aplicación.
2. ***View (Vista)***:
Representa la interfaz de usuario (lo que el usuario ve y con lo que interactúa).
3. ***ViewModel***:
Actúa como un puente entre el Model y la View.

## CÓDIGO

Vamos a explicar como está estructurado nuestro código y las funciones del mismo.

### 1. MODEL
* **Datos.kt**
  ```kotlin
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
    ```
    Gestión centralizada de los datos del juego (Singleton):
  - **`record`**: Récord máximo del jugador.
  - **`score`**: Puntuación actual.
  - **`ronda`**: Ronda actual.
  - **`secuenciaMaquina`**: Lista con la secuencia generada por la máquina.
  - **`secuenciaJugador`**: Lista con la secuencia ingresada por el jugador.
  - **`isPrinted`**: Estado observable para gestionar la visualización de colores.

  **Método principal**:
  - **`reset()`**: Reinicia puntuación, ronda y secuencias (sin afectar el récord).

  ---

  ### Enum `estados`
  Define los estados del juego y su comportamiento:
  - **`ESPERANDO`**: Espera el inicio del juego.
  - **`COLOREANDO`**: Muestra la secuencia generada.
  - **`JUGANDO`**: El jugador introduce la secuencia.
  - **`PERDIDO`**: Fin del juego; permite reiniciar.

  Cada estado controla:
  - Si el botón de inicio está activo.
  - Si los botones de colores están habilitados.
  - Si se debe mostrar la secuencia en pantalla.

### 2. MODELVIEW
* **MyViewModel.kt**
  
  ```kotlin
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
    ```
    Gestión de la lógica del juego en el modelo MVVM.

    #### Variables observables (StateFlow):
    - **`estado`**: Controla el estado actual del juego (`ESPERANDO`, `COLOREANDO`, etc.).
    - **`score`**: Puntuación actual del jugador.
    - **`ronda`**: Ronda actual del juego.
    - **`record`**: Récord máximo alcanzado.

    #### Métodos principales:
    - **`iniciarJuego()`**: 
    - Reinicia los datos del juego.
    - Genera la secuencia inicial de la máquina.
    - Cambia el estado a `COLOREANDO` para mostrar la secuencia.

    - **`actualizarNumero(num: Int)`**:
    - Actualiza la secuencia ingresada por el jugador.
    - Verifica si la secuencia es correcta.
    - Si es correcta:
        - Incrementa la puntuación y la ronda.
        - Añade un nuevo número a la secuencia de la máquina.
        - Cambia el estado a `COLOREANDO`.
    - Si es incorrecta, cambia el estado a `PERDIDO`.

    - **`crearNumerosRandom()`**:
    - Añade un número aleatorio (0-3) a la secuencia de la máquina.

    - **`resetGame()`**:
    - Reinicia todos los datos del juego y el estado a `ESPERANDO`.

    - **`reproducirSecuenciaMaquina(onColorChange: (Int) -> Unit)`**:
    - Reproduce la secuencia generada por la máquina.
    - Cambia el color mostrado paso a paso, con pausas entre cada color.
    - Cambia el estado a `JUGANDO` al terminar de mostrar la secuencia.

### 3. VIEW
* **UI.kt**
    ```kotlin
    package com.example.simondice2_estebanmontes.view

    import android.util.Log
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
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
        val estado by viewModel.estado.collectAsState()
        val secuenciaMaquina = Datos.secuenciaMaquina
        var backgroundColor by remember { mutableStateOf(Color.White) }

        // Cambiar el fondo según la secuencia de la máquina
        LaunchedEffect(estado) {
            if (estado == estados.COLOREANDO) {
                viewModel.reproducirSecuenciaMaquina { colorIndex ->
                    backgroundColor = when (colorIndex) {
                        0 -> Colores.ROJO.color
                        1 -> Colores.VERDE.color
                        2 -> Colores.AZUL.color
                        3 -> Colores.AMARILLO.color
                        else -> Color.White
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
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

                val mensaje = when (estado) {
                    estados.ESPERANDO -> "Pulsa Start para comenzar"
                    estados.COLOREANDO -> "Observa la secuencia"
                    estados.JUGANDO -> "Tu turno: sigue la secuencia"
                    estados.PERDIDO -> "Perdiste. Pulsa Start para intentarlo de nuevo"
                    else -> ""
                }

                Text(text = mensaje, fontSize = 18.sp, modifier = Modifier.padding(16.dp))

                if (estado == estados.ESPERANDO || estado == estados.PERDIDO) {
                    BotonStart(viewModel)
                } else {
                    botonColores(viewModel, -1)
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
    fun textoPuntuacion(viewModel: MyViewModel) {
        val puntuacion by viewModel.score.collectAsState()
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
    fun textoRonda(viewModel: MyViewModel) {
        val ronda by viewModel.ronda.collectAsState()
        Text(
            text = "Ronda: $ronda",
            fontSize = 20.sp
        )
    }

    @Composable
    fun textoRecord(viewModel: MyViewModel) {
        val record by viewModel.record.collectAsState()
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
    ```
    #### Descripción general
    La clase define la interfaz de usuario del juego siguiendo el patrón MVVM. Aquí se manejan las interacciones del usuario, los eventos visuales y la conexión con el `ViewModel`.

    #### Componentes principales

    - **Fondo dinámico**:
    - Cambia el color del fondo según la secuencia generada por la máquina (`estado.COLOREANDO`).

    - **Estados y Mensajes**:
    - Muestra mensajes dinámicos según el estado del juego (`ESPERANDO`, `COLOREANDO`, `JUGANDO`, `PERDIDO`).

    - **Botones de interacción**:
    - **`BotonStart`**: Inicia el juego. Cambia de color dinámicamente mientras está disponible.
    - **`botonColores`**: Botones para los colores (rojo, verde, azul, amarillo). Los usuarios interactúan con ellos para seguir la secuencia.

    #### Funciones auxiliares

    - **`textoPuntuacion`**: Muestra la puntuación actual del usuario.
    - **`textoRonda`**: Indica la ronda actual del juego.
    - **`textoRecord`**: Muestra el récord más alto alcanzado.

    #### Vista previa (`Preview`)
    - Una función de ejemplo (`PreviewApp`) permite visualizar la interfaz en el editor con datos iniciales.

    #### Estados gestionados:
    1. **`ESPERANDO`**: Muestra un botón para iniciar el juego.
    2. **`COLOREANDO`**: El fondo cambia para mostrar la secuencia de la máquina.
    3. **`JUGANDO`**: Permite interactuar con los botones de colores.
    4. **`PERDIDO`**: Muestra un mensaje de pérdida y permite reiniciar.

    #### Conexión con `MyViewModel`
    La interfaz está sincronizada con el ViewModel a través de `StateFlow` para obtener la puntuación, ronda, récord y estado actual del juego.

## ¿CÓMO JUGAR?
#### Objetivo del juego
Repite la secuencia de colores generada por la máquina para avanzar de ronda. Cada ronda se agrega un nuevo color a la secuencia, ¡pon a prueba tu memoria y alcanza la mayor puntuación posible!

#### Instrucciones

1. **Iniciar el juego**:
   - Pulsa el botón **Start** en la pantalla inicial. 
   - El juego comienza mostrando una secuencia de colores en pantalla.

2. **Observar la secuencia**:
   - Presta atención al orden en el que los colores se iluminan.
   - Durante este momento, el mensaje en pantalla dirá: **"Observa la secuencia"**.

3. **Reproducir la secuencia**:
   - Después de que la secuencia termine, el mensaje cambiará a **"Tu turno: sigue la secuencia"**.
   - Toca los botones de colores (rojo, verde, azul, amarillo) en el mismo orden mostrado por la máquina.

4. **Superar rondas**:
   - Si reproduces correctamente la secuencia, avanzarás a la siguiente ronda.
   - En cada ronda, la máquina agregará un nuevo color a la secuencia.
   - Tu puntuación y ronda aumentarán.

5. **Perder el juego**:
   - Si te equivocas en la secuencia, el mensaje dirá: **"Perdiste. Pulsa Start para intentarlo de nuevo"**.
   - Podrás reiniciar el juego pulsando el botón **Start**.

6. **Lograr un récord**:
   - Si tu puntuación supera el récord anterior, este se actualizará automáticamente.

#### Controles

- **Botón Start**:
  - Inicia una nueva partida.
  - Cambia de color de manera dinámica mientras está disponible.

- **Botones de colores**:
  - Representan los colores **rojo**, **verde**, **azul** y **amarillo**.
  - Usa estos botones para reproducir la secuencia que muestra la máquina.

#### Estados visuales del juego

1. **Blanco (predeterminado)**: Fondo inicial.
2. **Colores dinámicos**: El fondo cambia temporalmente para mostrar la secuencia de la máquina.
3. **Botones de colores**: Se activan solo en el estado **JUGANDO** para que reproduzcas la secuencia.

#### Consejos para jugar

- Memoriza cuidadosamente el orden de los colores.
- Concéntrate y no te apresures a pulsar los botones.
- Cuanto más practiques, ¡mejor será tu memoria!

#### ¡A jugar!
Inicia el juego y trata de superar tu récord. ¿Hasta qué ronda puedes llegar?