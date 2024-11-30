package io.github.chethann.spanlayout

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "SpanLayout",
    ) {
        App()
    }
}