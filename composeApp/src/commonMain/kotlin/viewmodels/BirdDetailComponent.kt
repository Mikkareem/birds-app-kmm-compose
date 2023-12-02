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
import screenevents.BirdDetailsScreenEvent

class BirdDetailComponent(
    private val birdId: Long,
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit
): ComponentContext by componentContext, KoinComponent {

    private val _bird = MutableValue(Bird())
    val bird: Value<Bird> = _bird

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
        fetchBirdDetails()
    }

    private fun fetchBirdDetails() {
        _isLoading.update { true }
        _networkError.update { false }
        componentScope().launch(coroutineExceptionHandler) {
            val bird = api.getBirdDetailsById(birdId)
            _bird.update { bird }
        }.invokeOnCompletion {
            _isLoading.update { false }
        }
    }

    fun onEvent(event: BirdDetailsScreenEvent) {
        when(event) {
            BirdDetailsScreenEvent.GoBack -> onNavigateBack()
            BirdDetailsScreenEvent.RetryFetchDetails -> fetchBirdDetails()
        }
    }
}