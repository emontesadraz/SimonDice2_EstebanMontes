package com.example.simondice2_estebanmontes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.simondice2_estebanmontes.ui.theme.SimonDice2_EstebanMontesTheme
import com.example.simondice2_estebanmontes.view.App
import com.example.simondice2_estebanmontes.modelview.MyViewModel
import com.example.simondice2_estebanmontes.view.crearBotones

class MainActivity : ComponentActivity() {
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimonDice2_EstebanMontesTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MyViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        App(viewModel = viewModel, paddingValues = paddingValues)
    }
}

@Composable
fun App(viewModel: MyViewModel, paddingValues: PaddingValues) {
    Column(modifier = Modifier.padding(paddingValues)) {
        crearBotones(viewModel)
    }
}

@Composable
@Preview
fun PreviewMainScreen() {
    val viewModel = MyViewModel()
    SimonDice2_EstebanMontesTheme {
        MainScreen(viewModel)
    }
}