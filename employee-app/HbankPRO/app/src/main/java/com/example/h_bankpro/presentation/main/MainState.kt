package com.example.h_bankpro.presentation.main

import androidx.paging.PagingData
import com.example.h_bankpro.data.Rate
import com.example.h_bankpro.domain.model.Tariff
import com.example.h_bankpro.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

data class MainState(
    val initialTariffs: List<Tariff> = emptyList(),
    val tariffsFlow: Flow<PagingData<Tariff>> = MutableStateFlow(PagingData.empty()),
    val users: List<User> = emptyList(),
    val currentUserId: String = "",
    val rates: List<Rate> = listOf(
        Rate(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Ставка А",
            interestRate = 5.75f,
            description = "Базовая ставка для новых клиентов"
        ),
        Rate(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Ставка А",
            interestRate = 5.75f,
            description = "Базовая ставка для новых клиентов"
        ),
        Rate(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Ставка А",
            interestRate = 5.75f,
            description = "Базовая ставка для новых клиентов"
        ),
        Rate(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Ставка А",
            interestRate = 5.75f,
            description = "Базовая ставка для новых клиентов"
        ),
    ),
    val isUsersSheetVisible: Boolean = false,
    val isTariffsSheetVisible: Boolean = false,
    val isLoading: Boolean = false
)