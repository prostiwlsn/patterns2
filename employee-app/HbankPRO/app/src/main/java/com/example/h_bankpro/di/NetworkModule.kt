package com.example.h_bankpro.di

import com.example.h_bankpro.data.network.AuthInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single<Retrofit>(named("firstApi")) {
        Retrofit.Builder()
            .client(get())
            .baseUrl("http://194.59.186.122:8080/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<Retrofit>(named("yuraApi")) {
        Retrofit.Builder()
            .client(get(named("authClient")))
            .baseUrl("http://31.129.110.211:8080/api/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single(qualifier = named("authClient")) {
        get<OkHttpClient>().newBuilder()
            .addInterceptor(AuthInterceptor(get(), get()))
            .build()
    }

    single<Retrofit>(named("firstApiWithAuth")) {
        Retrofit.Builder()
            .client(get(named("authClient")))
            .baseUrl("http://194.59.186.122:8080/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
