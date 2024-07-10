package com.example.swiftscribe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.swiftscribe.ui.NoteViewModel
import com.example.swiftscribe.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<NoteViewModel>(factoryProducer = { NoteViewModel.Factory })

        setContent {
            MainScreen(viewModel)
        }
    }
}