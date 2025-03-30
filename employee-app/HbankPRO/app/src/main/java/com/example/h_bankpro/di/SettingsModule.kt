package com.example.h_bankpro.di

import com.example.h_bankpro.data.network.SettingsApi
import com.example.h_bankpro.data.settings.SettingsRepository
import com.example.h_bankpro.data.settings.SettingsRepositoryImpl
import com.example.h_bankpro.domain.useCase.SettingsUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val settingsModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl(get(), get()) }

    factory<SettingsUseCase> {
        SettingsUseCase(
            settingsRepository = get()
        )
    }

    factory<SettingsApi> {
        val retrofit = get<Retrofit>(named("settingsApi"))
        retrofit.create(SettingsApi::class.java)
    }
}