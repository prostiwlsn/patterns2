package com.example.h_bankpro.data.mapper

import com.example.h_bankpro.data.dto.LoginDto
import com.example.h_bankpro.domain.entity.CredentialsEntity

fun CredentialsEntity.toLoginRequestDto() = LoginDto(
    password = this.password.orEmpty(),
    phone = "+7" + this.phoneNumber.orEmpty(),
)