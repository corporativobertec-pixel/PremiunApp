package com.premium.app.models

data class User(
    val uid: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val birthdate: String = "",
    val profileImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
