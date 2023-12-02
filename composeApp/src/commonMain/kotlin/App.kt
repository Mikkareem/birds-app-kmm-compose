import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import navigation.RootComponent
import screens.BirdDetailScreen
import screens.BirdsListScreen

@Composable
fun App(
    root: RootComponent
) {
    MaterialTheme {
        val childStack by root.childStack.subscribeAsState()

        Children(
            stack = childStack,
            animation = stackAnimation(slide())
        ) { child ->
            when(val instance = child.instance) {
                is RootComponent.Child.BirdDetailScreen -> BirdDetailScreen(instance.component)
                is RootComponent.Child.BirdsListScreen -> BirdsListScreen(instance.component)
            }
        }
    }
}

fun ComponentContext.componentScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    if(lifecycle.state == Lifecycle.State.DESTROYED) {
        scope.cancel()
    } else {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    return scope
}