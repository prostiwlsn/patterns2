package com.example.h_bank.di

import com.example.h_bank.data.network.SettingsApi
import com.example.h_bank.data.repository.settings.SettingsRepository
import com.example.h_bank.data.repository.settings.SettingsRepositoryImpl
import com.example.h_bank.domain.useCase.SettingsUseCase
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