package com.mrwhoknows.feature.short_url.domain

import com.mrwhoknows.feature.short_url.domain.model.ShortUrlDTO

interface ShortUrlRepository {
    suspend fun getAllShortUrls(): List<ShortUrlDTO>
    // TODO
}