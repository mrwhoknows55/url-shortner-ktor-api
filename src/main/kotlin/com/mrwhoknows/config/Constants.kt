package com.mrwhoknows.config

object Constants {
    val DB_URL = System.getenv("DB_URL") ?: "mongodb://localhost"
    val PORT = System.getenv("PORT")?.toInt() ?: 8080
}
