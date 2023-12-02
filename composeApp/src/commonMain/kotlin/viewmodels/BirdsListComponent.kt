package viewmodels

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import componentScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.Bird
import network.NuthatchApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import screenevents.BirdsListScreenEvent

class BirdsListComponent(
    componentContext: ComponentContext,
    private val onNavigateToBirdsListScreen: (Long) -> Unit
): ComponentContext by componentContext, KoinComponent {

    private val _birds: MutableValue<List<Bird>> = MutableValue(emptyList())
    val birds: Value<List<Bird>> = _birds

    private val _networkError = MutableValue(false)
    val networkError: Value<Boolean> = _networkError

    private val _isLoading = MutableValue(true)
    val isLoading: Value<Boolean> = _isLoading

    private val api by inject<NuthatchApi>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if(throwable::class.simpleName!!.contains("UnknownHostException")) {
            _networkError.update { true }
        } else {
            println("Uncaughtable Exception $throwable")
        }
    }

    init {
        fetchBirds()
    }

    private fun fetchBirds() {
        _isLoading.update { true }
        _networkError.update { false }
        componentScope().launch(coroutineExceptionHandler) {
            delay(5000)
            val tBirds = api.getAllBirds()
            _birds.update { tBirds }
        }.invokeOnCompletion {
            _isLoading.update { false }
        }
    }

    fun onEvent(event: BirdsListScreenEvent) {
        when(event) {
            is BirdsListScreenEvent.ClickOnBird -> onNavigateToBirdsListScreen(event.id)
            BirdsListScreenEvent.RetryFetch -> fetchBirds()
        }
    }
}