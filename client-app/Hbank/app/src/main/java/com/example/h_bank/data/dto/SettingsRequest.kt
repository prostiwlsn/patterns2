package com.example.h_bank.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SettingsRequest(val theme: String, val hiddenAccounts: List<String>)
