package com.mrwhoknows

import com.mrwhoknows.data.model.ShortUrl
import com.mrwhoknows.data.repo.ShortUrlRepo
import com.mrwhoknows.data.repo.ShortUrlRepoImpl
import com.mrwhoknows.routes.configureRoutes
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main() {
    // TODO: create constants and env vars
    val shortUrlConnection = KMongo.createClient().coroutine.getDatabase("short-url").getCollection<ShortUrl>()
    val repository: ShortUrlRepo = ShortUrlRepoImpl(shortUrlConnection)
    val port = System.getenv("PORT").toInt()
    val PORT = if (port < 0) 80 else port
    embeddedServer(Netty, port = PORT, host = "0.0.0.0") {
        // TODO: introduce DI
        configureRoutes(repository)
    }.start(wait = true)
}
