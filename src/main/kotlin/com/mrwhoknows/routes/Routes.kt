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
                val result = repository.getShortUrl(shortUrl)
                result?.shortUrl?.let {
                    result.increaseVisitCount()
                    val isUpdated = repository.updateShortUrl(result.id, result)
                    if (isUpdated) call.respondRedirect(result.longUrl, permanent = false)
                    else call.respond(
                        status = HttpStatusCode.InternalServerError,
                        Pair("result", "Something went wrong")
                    )
                    return@get
                } ?: kotlin.run {
                    call.respond(
                        status = HttpStatusCode.InternalServerError, Pair("result", "Something went wrong")
                    )
                    return@get
                }
            } else {
                call.respond(status = HttpStatusCode.NotFound, "ShortUrl doesn't exists")
            }
        }

        route("/api/v1/") {
            shortUrlRoute("short-url", repository)
        }
    }
}

fun Route.shortUrlRoute(path: String, repository: ShortUrlRepo) {
    route(path) {

        post {
            val body = call.receive<ShortUrl>()
            val isAlreadyPresent = repository.doesShortUrlExists(body.shortUrl)

            if (isAlreadyPresent) {
                call.respond(Pair("result", "failed, short_url already present, please try another one"))
                return@post
            }
            val isCreated = repository.createShortUrl(body)
            if (isCreated) {
                val inserted = repository.getShortUrl(body.shortUrl)
                call.respond(inserted ?: Pair("result", "failed"))
            } else {
                call.respond(Pair("result", "failed"))
            }
        }

        get {
            try {
                val shortUrls = repository.getShortUrls()
                call.respond(shortUrls)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // TODO: Error and edge case handling
    route("$path/id/{id}") {

        get {
            val id = call.parameters["id"]?.toInt()
            if (id != null) {
                val result = repository.getShortUrl(id)
                result?.let {
                    call.respond(it)
                } ?: kotlin.run {
                    call.respond(Pair("result", "Something went wrong"))
                }
            } else {
                call.respond(status = HttpStatusCode.NotFound, "ShortUrl doesn't exists")
            }
        }

        delete {
            val id = call.parameters["id"]?.toInt()
            if (id != null) {
                val result = repository.deleteShortUrl(id)
                call.respond(Pair("result", result))
            } else {
                call.respond(status = HttpStatusCode.NotFound, "ShortUrl doesn't exists")
            }
        }

        put {
            val id = call.parameters["id"]?.toInt()
            if (id != null) {
                val body = call.receive<ShortUrl>()
                val isUpdated = repository.updateShortUrl(id, body)
                if (isUpdated) {
                    call.respond(Pair("result", "updated successfully"))
                    return@put
                }
                call.respond(HttpStatusCode.ExpectationFailed, Pair("result", "update not successful"))
                return@put
            }
            call.respond(status = HttpStatusCode.NotFound, "ShortUrl doesn't exists")
        }
    }

    // TODO: Error and edge case handling
    route("$path/short-url/{short-url}") {

        get {
            val shortUrl = call.parameters["short-url"] ?: kotlin.run {
                call.respond(status = HttpStatusCode.BadRequest, Pair("result", "Wrong short url"))
                return@get
            }
            val result = repository.getShortUrl(shortUrl)
            result?.let {
                call.respond(it)
            } ?: kotlin.run {
                call.respond(Pair("result", "Something went wrong"))
            }
        }

        delete {
            val shortUrl = call.parameters["short-url"] ?: kotlin.run {
                call.respond(status = HttpStatusCode.BadRequest, Pair("result", "Wrong short url"))
                return@delete
            }
            val result = repository.deleteShortUrl(shortUrl)
            call.respond(Pair("result", result))
        }

        put {
            val shortUrl = call.parameters["short-url"] ?: kotlin.run {
                call.respond(status = HttpStatusCode.BadRequest, Pair("result", "Wrong short url"))
                return@put
            }
            val body = call.receive<ShortUrl>()
            val isUpdated = repository.updateShortUrl(shortUrl, body)
            if (isUpdated) {
                call.respond(Pair("result", "updated successfully"))
                return@put
            }
            call.respond(HttpStatusCode.ExpectationFailed, Pair("result", "update not successful"))
        }
    }
}
