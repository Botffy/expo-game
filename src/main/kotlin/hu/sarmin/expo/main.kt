package hu.sarmin.expo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import hu.sarmin.expo.game.GameViewModel

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "expo",
        state = WindowState(size = DpSize(800.dp, 600.dp)),
    ) {
        val scope = rememberCoroutineScope()
        val viewModel = remember { GameViewModel(scope) }
        val state by viewModel.state.collectAsState()

        val listState = rememberLazyListState()

        // Auto-scroll to the latest plated dish
        LaunchedEffect(state.platedDishes.size) {
            if (state.platedDishes.isNotEmpty()) {
                listState.animateScrollToItem(state.platedDishes.lastIndex)
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text(
                text = "Sim time: ${state.simTime}",
                style = MaterialTheme.typography.h5,
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Window",
                style = MaterialTheme.typography.h6,
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (state.platedDishes.isEmpty()) {
                Text(
                    text = "Nothing plated yet.",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                )
            } else {
                LazyColumn(state = listState) {
                    items(state.platedDishes) { dish ->
                        Text(
                            text = "▸ $dish",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(vertical = 2.dp),
                        )
                    }
                }
            }
        }
    }
}
