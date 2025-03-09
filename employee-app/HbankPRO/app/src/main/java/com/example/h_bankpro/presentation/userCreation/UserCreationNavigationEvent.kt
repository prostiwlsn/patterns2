package com.example.h_bankpro.presentation.userCreation

sealed class UserCreationNavigationEvent {
    data object NavigateToSuccessfulUserCreation : UserCreationNavigationEvent()
    data object NavigateBack : UserCreationNavigationEvent()
}