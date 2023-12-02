package network

import com.techullurgy.composekmmtest.ApiKeyConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun provideHttpClient(): HttpClient = HttpClient {
    defaultRequest {
        headers.append("api-key", ApiKeyConfig.NuthatchApiKey)
    }

    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }
}