package com.example.h_bankpro.presentation.loan

sealed class LoanNavigationEvent {
    data object NavigateBack : LoanNavigationEvent()
}