package navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.serialization.Serializable
import viewmodels.BirdDetailComponent
import viewmodels.BirdsListComponent

class RootComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.BirdsListScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when(config) {
            is Configuration.BirdDetailScreen -> Child.BirdDetailScreen(
                component = BirdDetailComponent(
                    birdId = config.birdId,
                    componentContext = context,
                    onNavigateBack = { navigation.pop() }
                )
            )
            Configuration.BirdsListScreen -> Child.BirdsListScreen(
                component = BirdsListComponent(
                    componentContext = context,
                    onNavigateToBirdsListScreen = { navigation.pushNew(Configuration.BirdDetailScreen(it)) }
                )
            )
        }
    }

    sealed class Child {
        data class BirdsListScreen(val component: BirdsListComponent): Child()
        data class BirdDetailScreen(val component: BirdDetailComponent): Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object BirdsListScreen: Configuration()
        @Serializable
        data class BirdDetailScreen(val birdId: Long): Configuration()
    }
}