package com.example.h_bank.di

import com.example.h_bank.data.network.UserApi
import com.example.h_bank.data.repository.UserRepository
import com.example.h_bank.domain.repository.IUserRepository
import com.example.h_bank.domain.useCase.BlockUserUseCase
import com.example.h_bank.domain.useCase.CreateUserUseCase
import com.example.h_bank.domain.useCase.GetCurrentUserUseCase
import com.example.h_bank.domain.useCase.GetUserByIdUseCase
import com.example.h_bank.domain.useCase.GetUsersUseCase
import com.example.h_bank.domain.useCase.UnblockUserUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val userModule = module {
    single<IUserRepository> {
        UserRepository(
            api = get()
        )
    }

    factory<GetUsersUseCase> {
        GetUsersUseCase(
            userRepository = get()
        )
    }

    factory<GetCurrentUserUseCase> {
        GetCurrentUserUseCase(
            userRepository = get()
        )
    }

    factory<GetUserByIdUseCase> {
        GetUserByIdUseCase(
            userRepository = get()
        )
    }

    factory<CreateUserUseCase> {
        CreateUserUseCase(
            userRepository = get()
        )
    }

    factory<BlockUserUseCase> {
        BlockUserUseCase(
            userRepository = get()
        )
    }

    factory<UnblockUserUseCase> {
        UnblockUserUseCase(
            userRepository = get()
        )
    }

    factory<UserApi> {
        val retrofit = get<Retrofit>(named("firstApi"))
        retrofit.create(UserApi::class.java)
    }
}