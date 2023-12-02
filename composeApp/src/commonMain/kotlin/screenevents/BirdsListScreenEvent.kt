package screenevents

sealed interface BirdsListScreenEvent {
    data class ClickOnBird(val id: Long): BirdsListScreenEvent
    data object RetryFetch: BirdsListScreenEvent
}