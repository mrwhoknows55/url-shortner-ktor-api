package com.mrwhoknows.feature.short_url.domain

import com.mrwhoknows.feature.short_url.domain.model.ShortUrlDTO

class ShortUrlRepositoryImpl : ShortUrlRepository {
    override suspend fun getAllShortUrls(): List<ShortUrlDTO> {
        //        TODO("Not yet implemented")
        return emptyList()
    }
}