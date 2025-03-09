package com.example.h_bankpro.domain.entity

sealed interface Command {
    data object RefreshMainScreen : Command
    data object NavigateToNoConnection : Command
}