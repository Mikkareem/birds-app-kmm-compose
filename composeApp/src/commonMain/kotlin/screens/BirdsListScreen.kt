package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import screenevents.BirdsListScreenEvent
import screens.components.NetworkAwareContainer
import viewmodels.BirdsListComponent

@Composable
fun BirdsListScreen(
    component: BirdsListComponent
) {
    val birds by component.birds.subscribeAsState()

    val internetError by component.networkError.subscribeAsState()
    val loading by component.isLoading.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFFBF40BF)),
        contentAlignment = Alignment.Center
    ) {
        NetworkAwareContainer(
            loading = loading,
            internetError = internetError,
            onRetry = { component.onEvent(BirdsListScreenEvent.RetryFetch) }
        ) {
            Column {
                Text(
                    "Birds",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 16.dp, start = 16.dp)
                )
                LazyColumn {
                    items(birds) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    component.onEvent(BirdsListScreenEvent.ClickOnBird(it.id))
                                }
                                .padding(16.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(it.name, fontSize = 24.sp, color = Color.White)
                                Text(it.sciName, fontSize = 16.sp, color = Color.White)
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}