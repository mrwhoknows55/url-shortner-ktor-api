package com.mrwhoknows.feature.short_url.resource

import com.mrwhoknows.feature.short_url.domain.ShortUrlRepository
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@kotlinx.serialization.Serializable
@Resource("shortUrl")
class ShortUrlEndPoint {
    @kotlinx.serialization.Serializable
    @Resource("/all")
    class GetAll(val parent: ShortUrlEndPoint)
}

fun Route.shortUrlEndpoint() {
    val shortUrlRepository by inject<ShortUrlRepository>()

    get<ShortUrlEndPoint.GetAll> {
        call.respond(shortUrlRepository.getAllShortUrls())
    }
}