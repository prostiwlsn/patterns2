package com.example.h_bankpro.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.h_bankpro.data.dataSource.local.SettingsLocalDataSource
import com.example.h_bankpro.data.dataSource.remote.SettingsRemoteDataSource
import com.example.h_bankpro.data.network.SettingsApi
import com.example.h_bankpro.data.repository.settings.SettingsRepository
import com.example.h_bankpro.domain.repository.ISettingsRepository
import com.example.h_bankpro.domain.useCase.SettingsUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val settingsModule = module {
    single { SettingsLocalDataSource(dataStore = get<DataStore<Preferences>>()) }

    single { SettingsRemoteDataSource(settingsApi = get()) }

    single<ISettingsRepository> {
        SettingsRepository(
            localDataSource = get(),
            remoteDataSource = get()
        )
    }

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