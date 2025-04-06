package com.example.h_bank.di

import com.example.h_bank.data.dataSource.local.SettingsLocalDataSource
import com.example.h_bank.data.dataSource.remote.SettingsRemoteDataSource
import com.example.h_bank.data.network.SettingsApi
import com.example.h_bank.data.repository.settings.SettingsRepository
import com.example.h_bank.domain.repository.settings.ISettingsRepository
import com.example.h_bank.domain.useCase.SettingsUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val settingsModule = module {
    single<ISettingsRepository> {
        SettingsRepository(
            localDataSource = get(),
            remoteDataSource = get()
        )
    }

    single { SettingsLocalDataSource(dataStore = get()) }
    single { SettingsRemoteDataSource(settingsApi = get()) }

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