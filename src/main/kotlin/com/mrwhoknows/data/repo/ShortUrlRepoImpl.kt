package com.mrwhoknows.data.repo

import com.mongodb.client.model.UpdateOptions
import com.mrwhoknows.data.model.ShortUrl
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class ShortUrlRepoImpl(
    private val shortUrlDBCollection: CoroutineCollection<ShortUrl>,
) : ShortUrlRepo {

    override suspend fun getShortUrl(id: Int): ShortUrl? = shortUrlDBCollection.findOne(ShortUrl::id eq id)

    override suspend fun getShortUrl(shortUrl: String): ShortUrl? =
        shortUrlDBCollection.findOne(ShortUrl::shortUrl eq shortUrl)

    override suspend fun getShortUrls(): List<ShortUrl> = shortUrlDBCollection.find().toList()

    override suspend fun createShortUrl(shortUrl: ShortUrl): Boolean {
        return shortUrlDBCollection.insertOne(shortUrl).wasAcknowledged()
    }

    // TODO: Error and edge case handling
    override suspend fun updateShortUrl(oldShortUrlId: Int, shortUrl: ShortUrl): Boolean {
        val updatedObject = ShortUrl(
            id = oldShortUrlId,
            shortUrl = shortUrl.shortUrl,
            longUrl = shortUrl.longUrl,
            description = shortUrl.description,
            visitCount = shortUrl.getVisitCount(),
            name = shortUrl.name
        )
        return shortUrlDBCollection.updateOne(
            ShortUrl::id eq oldShortUrlId,
            updatedObject,
            updateOnlyNotNullProperties = true
        )
            .wasAcknowledged()
    }

    override suspend fun updateShortUrl(oldShortUrl: String, shortUrl: ShortUrl): Boolean {
        val oldObj = getShortUrl(oldShortUrl) ?: return false
        val updatedObject = ShortUrl(
            id = oldObj.id,
            shortUrl = shortUrl.shortUrl,
            longUrl = shortUrl.longUrl,
            description = shortUrl.description,
            visitCount = oldObj.getVisitCount(),
            name = shortUrl.name
        )
        return shortUrlDBCollection.updateOne(ShortUrl::shortUrl eq oldShortUrl, updatedObject).wasAcknowledged()
    }

    override suspend fun deleteShortUrl(shortUrlId: Int): Boolean =
        shortUrlDBCollection.deleteOne(ShortUrl::id eq shortUrlId).wasAcknowledged()

    override suspend fun deleteShortUrl(shortUrl: String): Boolean =
        shortUrlDBCollection.deleteOne(ShortUrl::shortUrl eq shortUrl).wasAcknowledged()

    override suspend fun doesShortUrlExists(shortUrl: String): Boolean =
        shortUrlDBCollection.findOne(ShortUrl::shortUrl eq shortUrl) != null
}
