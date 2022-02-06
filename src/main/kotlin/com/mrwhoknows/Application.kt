package com.mrwhoknows

import com.mrwhoknows.config.Constants.DB_URL
import com.mrwhoknows.config.Constants.PORT
import com.mrwhoknows.data.model.ShortUrl
import com.mrwhoknows.data.repo.ShortUrlRepo
import com.mrwhoknows.data.repo.ShortUrlRepoImpl
import com.mrwhoknows.routes.configureRoutes
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main() {
    val shortUrlConnection = KMongo.createClient(DB_URL).coroutine.getDatabase("short-url").getCollection<ShortUrl>()
    val repository: ShortUrlRepo = ShortUrlRepoImpl(shortUrlConnection)
    embeddedServer(Netty, port = PORT, host = "0.0.0.0") {
        // TODO: introduce DI
        configureRoutes(repository)
    }.start(wait = true)
}
