package com.example.h_bank.domain.entity.authorization

sealed interface Command {
    data object RefreshMainScreen : Command
    data object NavigateToNoConnection : Command
    data object NavigateToServerError : Command
    data object Logout : Command
}