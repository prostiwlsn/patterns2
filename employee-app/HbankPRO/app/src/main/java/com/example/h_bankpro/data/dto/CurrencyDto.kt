package com.example.h_bankpro.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CurrencyDto(val displayName: String) {
    @SerialName("Рубль")
    RUB("₽ Рубли"),

    @SerialName("Доллар США")
    USD("$ Доллары"),

    @SerialName("Армянский драм")
    AMD("֏ Армянские драмы");
}