package model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val user_id: Int? = null,
    val username: String? = null,
    val password: String? = null,
)