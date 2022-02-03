package com.mrwhoknows.data.repo

import com.mrwhoknows.data.model.ShortUrl
import com.mrwhoknows.data.repo.ShortUrlRepo
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class ShortUrlRepoImpl(
    private val shortUrlDBCollection: CoroutineCollection<ShortUrl>,
) : ShortUrlRepo {

    override suspend fun getShortUrlById(id: Int): ShortUrl? =
        shortUrlDBCollection.findOne(ShortUrl::id eq id)

    override suspend fun getShortUrlByShortUrl(shortUrl: String): ShortUrl? =
        shortUrlDBCollection.findOne(ShortUrl::shortUrl eq shortUrl)

    override suspend fun getShortUrls(): List<ShortUrl> = shortUrlDBCollection.find().toList()

    override suspend fun createShortUrl(shortUrl: ShortUrl): Boolean {
        return shortUrlDBCollection.insertOne(shortUrl).wasAcknowledged()
    }

    override suspend fun updateShortUrl(oldShortUrlId: Int, shortUrl: ShortUrl): ShortUrl? {

        if (shortUrlDBCollection.updateOne(ShortUrl::id eq shortUrl.id, shortUrl).wasAcknowledged())
            return getShortUrlById(oldShortUrlId)
        return null
    }

    override suspend fun deleteShortUrl(shortUrlId: Int): Boolean =
        shortUrlDBCollection.deleteOne(ShortUrl::id eq shortUrlId).wasAcknowledged()

    override suspend fun doesShortUrlExists(shortUrl: String): Boolean =
        shortUrlDBCollection.findOne(ShortUrl::shortUrl eq shortUrl) != null
}
