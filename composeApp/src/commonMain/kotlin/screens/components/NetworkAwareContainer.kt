package screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun NetworkAwareContainer(
    loading: Boolean,
    internetError: Boolean,
    onRetry: () -> Unit,
    onLoaded: @Composable () -> Unit
) {
    if(internetError) {
        Column {
            Text("No Internet Connection")
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    } else if (loading) {
        CircularProgressIndicator()
    } else {
        onLoaded()
    }
}