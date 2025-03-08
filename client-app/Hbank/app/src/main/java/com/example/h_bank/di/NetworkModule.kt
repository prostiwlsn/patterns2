package com.example.h_bank.di

import com.example.h_bank.data.network.AuthInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single(named("baseClient")) {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single(named("authClient")) {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            })
            .addInterceptor(AuthInterceptor(get()))
            .build()
    }

    single<Retrofit>(named("infoAuthApi")) {
        Retrofit.Builder()
            .client(get(named("baseClient")))
            .baseUrl("http://194.59.186.122:8080/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<Retrofit>(named("infoAuthApiWithAuth")) {
        Retrofit.Builder()
            .client(get(named("authClient")))
            .baseUrl("http://194.59.186.122:8080/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<Retrofit>(named("accountApi")) {
        Retrofit.Builder()
            .client(get(named("authClient")))
            .baseUrl("http://31.129.110.211:8080/api/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<Retrofit>(named("loanApi")) {
        Retrofit.Builder()
            .client(get(named("authClient")))
            .baseUrl("http://194.59.186.122:8040/api/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}