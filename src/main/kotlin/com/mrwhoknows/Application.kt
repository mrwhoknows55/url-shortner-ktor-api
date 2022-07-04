package com.mrwhoknows

import ch.qos.logback.classic.Logger
import com.mrwhoknows.config.AppConfig
import com.mrwhoknows.config.setupConfig
import com.mrwhoknows.di.appModule
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    setupConfig()
    val appConfig by inject<AppConfig>()

    if (!appConfig.serverConfig.isProd) {
        val root = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME) as Logger
        root.level = ch.qos.logback.classic.Level.TRACE
    }

    install(ContentNegotiation) {
        json()
    }

    install(CallLogging)

    routing {
        get("/") {
            call.respondText("Yes! Your API is working fine \uD83D\uDE80")
        }
    }
}
