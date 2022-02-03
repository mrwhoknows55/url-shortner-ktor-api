package com.mrwhoknows.data.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

@Serializable
data class ShortUrl(
    @BsonId val id: Int = Random().nextInt(),
    val name: String,
    val longUrl: String,
    val shortUrl: String = UUID.randomUUID().toString(),
    val description: String? = null,
    private var visitCount: Long = 0L,
) {
    fun getVisitCount(): Long = visitCount
    fun increaseVisitCount(): Long {
        visitCount++
        println("increaseVisitCount: $visitCount")
        return visitCount
    }
}
