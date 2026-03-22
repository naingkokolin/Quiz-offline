package model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val user_id: Int,
    val username: String,
    val password: String,
)