package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import screenevents.BirdDetailsScreenEvent
import screens.components.NetworkAwareContainer
import viewmodels.BirdDetailComponent

@Composable
fun BirdDetailScreen(
    component: BirdDetailComponent
) {
    val bird by component.bird.subscribeAsState()
    val loading by component.isLoading.subscribeAsState()
    val internetError by component.networkError.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFFAA336A)),
        contentAlignment = Alignment.Center
    ) {
        NetworkAwareContainer(
            loading = loading,
            internetError = internetError,
            onRetry = { component.onEvent(BirdDetailsScreenEvent.RetryFetchDetails) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(bird.images.isNotEmpty()) {
                    KamelImage(
                        resource = asyncPainterResource(data = bird.images[0]),
                        contentDescription = null,
//                        modifier = Modifier.size(100.dp)
                    )
                }
                Text("Bird Name: ${bird.name}")
                Button(onClick = { component.onEvent(BirdDetailsScreenEvent.GoBack) }) {
                    Text("Go Back")
                }
            }
        }
    }
}