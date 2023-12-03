package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
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
        modifier = Modifier.fillMaxSize().background(color = Color(0xFFAA336A)).padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        NetworkAwareContainer(
            loading = loading,
            internetError = internetError,
            onRetry = { component.onEvent(BirdDetailsScreenEvent.RetryFetchDetails) }
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {

                    items(bird.images) {
                        KamelImage(
                            resource = asyncPainterResource(data = it) {
                                coroutineContext = Job() + Dispatchers.IO
                            },
                            contentDescription = null,
                            onLoading = { progress -> CircularProgressIndicator(progress) },
                            onFailure = { ex -> Box(modifier = Modifier.background(Color.Red)) },
                            modifier = Modifier.height(100.dp).aspectRatio(1f)
                        )
                    }
                }
                CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.copy(color = Color.White, fontSize = 20.sp)) {
                    Text("Name: ${bird.name}")
                    Text("Scientific Name: ${bird.sciName}")
                    Text("Family: ${bird.family}")
                    Text("Order: ${bird.order}")
                    Text("Region: ${bird.region.joinToString(",").removeSuffix(",")}")
                    Text("Status: ${bird.status}")
                    bird.wingspanMin?.let { Text("Min. Wingspan: $it") }
                    bird.wingspanMax?.let { Text("Max. Wingspan: $it") }
                    bird.lengthMin?.let { Text("Min. Length: $it") }
                    bird.lengthMax?.let { Text("Max. Length: $it") }
                }
                Button(onClick = { component.onEvent(BirdDetailsScreenEvent.GoBack) }) {
                    Text("Go Back")
                }
            }
        }
    }
}