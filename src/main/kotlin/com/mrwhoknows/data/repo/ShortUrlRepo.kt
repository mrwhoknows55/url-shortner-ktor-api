package com.mrwhoknows.data.repo

import com.mrwhoknows.data.model.ShortUrl

interface ShortUrlRepo {
    suspend fun getShortUrl(id: Int): ShortUrl?

    suspend fun getShortUrl(shortUrl: String): ShortUrl?

    // TODO: add pagination
    suspend fun getShortUrls(): List<ShortUrl>

    suspend fun createShortUrl(shortUrl: ShortUrl): Boolean

    suspend fun updateShortUrl(oldShortUrlId: Int, shortUrl: ShortUrl): Boolean

    suspend fun updateShortUrl(oldShortUrl: String, shortUrl: ShortUrl): Boolean

    suspend fun deleteShortUrl(shortUrlId: Int): Boolean

    suspend fun deleteShortUrl(shortUrl: String): Boolean

    suspend fun doesShortUrlExists(shortUrl: String): Boolean
}
