package com.example.h_bank.data.mapper

import com.example.h_bank.data.dto.LoginDto
import com.example.h_bank.data.dto.RegisterDto
import com.example.h_bank.domain.entity.CredentialsEntity

fun CredentialsEntity.toLoginRequestDto() = LoginDto(
    password = this.password.orEmpty(),
    phone = "+7" + this.phoneNumber.orEmpty(),
)

fun CredentialsEntity.toRegisterRequestDto() = RegisterDto(
    name = this.name.orEmpty(),
    password = this.password.orEmpty(),
    phone = "+7" + this.phoneNumber.orEmpty(),
)