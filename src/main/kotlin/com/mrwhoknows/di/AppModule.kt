package com.mrwhoknows.di

import com.mrwhoknows.config.AppConfig
import org.koin.dsl.module


val appModule = module {
    single { AppConfig() }
}