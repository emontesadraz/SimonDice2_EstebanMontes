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


