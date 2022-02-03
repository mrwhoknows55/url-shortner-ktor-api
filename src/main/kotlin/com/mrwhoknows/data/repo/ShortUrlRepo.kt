package com.mrwhoknows.data.repo

import com.mrwhoknows.data.model.ShortUrl

interface ShortUrlRepo {
    suspend fun getShortUrlById(id: Int): ShortUrl?

    suspend fun getShortUrlByShortUrl(shortUrl: String): ShortUrl?

    // TODO: add pagination
    suspend fun getShortUrls(): List<ShortUrl>

    suspend fun createShortUrl(shortUrl: ShortUrl): Boolean

    suspend fun updateShortUrl(oldShortUrlId: Int, shortUrl: ShortUrl): ShortUrl?

    suspend fun deleteShortUrl(shortUrlId: Int): Boolean

    suspend fun doesShortUrlExists(shortUrl: String): Boolean
}
