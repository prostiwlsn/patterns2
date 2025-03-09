package com.example.h_bank.domain.entity.authorization

sealed interface Command {
    data object UpdateProfile : Command
    data object NavigateToNoConnection : Command
}