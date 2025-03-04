package com.example.h_bankpro.presentation.common.utils

import androidx.compose.ui.text.AnnotatedString

object Utils {
    fun String.toAnnotated() = AnnotatedString(this)
    fun CharSequence.toAnnotated() = AnnotatedString(this.toString())
}