package network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import models.Bird

internal interface NuthatchApi {
    suspend fun getAllBirds(): List<Bird>
    suspend fun getBirdDetailsById(id: Long): Bird
}

internal class NuthatchApiImpl(
    private val client: HttpClient
): NuthatchApi {
    override suspend fun getAllBirds(): List<Bird> {
        return withContext(Dispatchers.IO) {
            val response = client.get("https://nuthatch.lastelm.software/v2/birds")
            @Serializable data class Response(val entities: List<Bird>)
            val birds = response.body<Response>()
            birds.entities
        }
    }

    override suspend fun getBirdDetailsById(id: Long): Bird {
        return withContext(Dispatchers.IO) {
            val response = client.get("https://nuthatch.lastelm.software/birds/$id")
            val bird = response.body<Bird>()
            bird
        }
    }
}