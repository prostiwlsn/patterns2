package com.example.h_bank.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CurrencyDto(val displayName: String) {
    @SerialName("rub")
    RUB("Рубли"),

    @SerialName("usd")
    USD("Доллары"),

    @SerialName("amd")
    AMD("Армянские драмы");
}