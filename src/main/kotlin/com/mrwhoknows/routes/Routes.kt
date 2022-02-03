package com.mrwhoknows.routes

import com.mrwhoknows.data.model.ShortUrl
import com.mrwhoknows.data.repo.ShortUrlRepo
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutes(repository: ShortUrlRepo) {

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respond("ShortUrl API is working")
        }

        get("/{shortUrl}") {
            val shortUrl = call.parameters["shortUrl"]
            if (!shortUrl.isNullOrBlank()) {
                try {
                    val result = repository.getShortUrlByShortUrl(shortUrl)
                    result?.shortUrl?.let {
                        result.increaseVisitCount()
                        val update = repository.updateShortUrl(result.id, result)
                        update?.let {
                            call.respondRedirect(it.longUrl, permanent = false)
                        } ?: kotlin.run {
                            throw Exception("Something went wrong")
                        }
                    } ?: kotlin.run {
                        throw Exception("Something went wrong")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(Pair("error", e.cause))
                }
            } else {
                call.respond(status = HttpStatusCode.NotFound, "ShortUrl doesn't exists")
            }
        }

        route("/api/v1/") {

            post("/short-url") {
                val body = call.receive<ShortUrl>()
                val isAlreadyPresent = repository.doesShortUrlExists(body.shortUrl)

                if (isAlreadyPresent) {
                    call.respond(Pair("result", "failed, short_url already present, please try another one"))
                    return@post
                }
                val isCreated = repository.createShortUrl(body)
                if (isCreated) {
                    val inserted = repository.getShortUrlByShortUrl(body.shortUrl)
                    call.respond(inserted ?: Pair("result", "failed"))
                } else {
                    call.respond(Pair("result", "failed"))
                }
            }

            get("/short-url") {
                try {
                    val shortUrls = repository.getShortUrls()
                    call.respond(shortUrls)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            get("/short-url/{id}") {
                val id = call.parameters["id"]?.toInt()
                if (id != null) {
                    val result = repository.getShortUrlById(id)
                    result?.let {
                        call.respond(it)
                    } ?: kotlin.run {
                        call.respond(Pair("result", "Something went wrong"))
                    }
                } else {
                    call.respond(status = HttpStatusCode.NotFound, "ShortUrl doesn't exists")
                }
            }
        }
    }
}
