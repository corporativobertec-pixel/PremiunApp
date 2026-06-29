package com.premium.app.models

data class Post(
    val id: String = "",
    val userId: String = "",
    val videoUrl: String = "",
    val thumbnailUrl: String = "",
    val description: String = "",
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val isOwner: Boolean = false
)
