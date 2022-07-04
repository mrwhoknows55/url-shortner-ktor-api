package com.mrwhoknows.feature.short_url.domain.model

@kotlinx.serialization.Serializable
data class ShortUrlDTO(
    val shortUrlId: String,
    val shortUrl: String,
    val longUrl: String,
    val totalVisits: Long = 0,
)