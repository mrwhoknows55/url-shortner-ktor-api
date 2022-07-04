package com.mrwhoknows.di

import com.mrwhoknows.config.AppConfig
import com.mrwhoknows.feature.short_url.domain.ShortUrlRepository
import com.mrwhoknows.feature.short_url.domain.ShortUrlRepositoryImpl
import org.koin.dsl.module


val appModule = module {
    single { AppConfig() }
    single<ShortUrlRepository> { ShortUrlRepositoryImpl() }
}