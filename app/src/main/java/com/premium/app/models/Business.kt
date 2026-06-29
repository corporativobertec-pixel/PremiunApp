package com.premium.app.models

data class Business(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val description: String = "",
    val phone: String = "",
    val address: String = "",
    val hours: String = "",
    val socialMedia: Map<String, String> = emptyMap(),
    val imageUrls: List<String> = emptyList()
)
