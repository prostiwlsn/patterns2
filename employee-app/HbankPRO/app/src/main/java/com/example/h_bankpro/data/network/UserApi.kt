package com.example.h_bankpro.data.network

import com.example.h_bankpro.data.dto.UserCreationDto
import com.example.h_bankpro.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @GET("users")
    suspend fun getUsers(
    ): List<UserDto>

    @GET("profile")
    suspend fun getCurrentUser(
    ): UserDto

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") userId: String,
    ): UserDto

    @POST("users")
    suspend fun createUser(
        @Body request: UserCreationDto
    )

    @POST("users/{id}/block")
    suspend fun blockUser(
        @Path("id") userId: String,
    )

    @POST("users/{id}/unblock")
    suspend fun unblockUser(
        @Path("id") userId: String,
    )
}