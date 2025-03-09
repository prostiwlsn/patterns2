package com.example.h_bank.domain.entity.authorization

sealed interface AuthorizationCommand {
    data object UpdateProfile : AuthorizationCommand
}