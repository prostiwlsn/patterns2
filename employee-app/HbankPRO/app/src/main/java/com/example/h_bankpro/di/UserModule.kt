package com.example.h_bankpro.di

import com.example.h_bankpro.data.network.UserApi
import com.example.h_bankpro.data.repository.UserRepository
import com.example.h_bankpro.domain.repository.IUserRepository
import com.example.h_bankpro.domain.useCase.BlockUserUseCase
import com.example.h_bankpro.domain.useCase.CreateUserUseCase
import com.example.h_bankpro.domain.useCase.GetCurrentUserUseCase
import com.example.h_bankpro.domain.useCase.GetUserByIdUseCase
import com.example.h_bankpro.domain.useCase.GetUsersUseCase
import com.example.h_bankpro.domain.useCase.UnblockUserUseCase
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
        val retrofit = get<Retrofit>(named("firstApiWithAuth"))
        retrofit.create(UserApi::class.java)
    }
}