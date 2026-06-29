package com.premium.app.models

data class Ad(
    val id: String = "",
    val businessId: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val targetUrl: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
