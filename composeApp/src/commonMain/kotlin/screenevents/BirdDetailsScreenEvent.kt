package screenevents

sealed interface BirdDetailsScreenEvent {
    data object GoBack: BirdDetailsScreenEvent
    data object RetryFetchDetails: BirdDetailsScreenEvent
}