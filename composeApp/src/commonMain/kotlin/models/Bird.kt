package models

import kotlinx.serialization.Serializable

@Serializable
data class Bird(
    val id: Long = 0,
    val name: String = "",
    val sciName: String = "",
    val order: String = "",
    val family: String = "",
    val status: String = "",
    val region: List<String> = emptyList(),
    val wingspanMin: String? = null,
    val wingspanMax: String? = null,
    val lengthMin: String? = null,
    val lengthMax: String? = null,
    val images: List<String> = emptyList()
)
