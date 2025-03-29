package com.example.h_bank.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.OptIn
import androidx.browser.auth.AuthTabIntent
import androidx.browser.auth.ExperimentalAuthTab

class AuthTabIntentContract : ActivityResultContract<Uri, AuthResult>() {
    @OptIn(ExperimentalAuthTab::class)
    override fun createIntent(context: Context, input: Uri): Intent {
        val authTabIntent = AuthTabIntent.Builder().build()
        return authTabIntent.intent.apply {
            data = input
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): AuthResult {
        val resultUri = intent?.data
        return AuthResult(resultCode, resultUri)
    }
}

data class AuthResult(val resultCode: Int, val resultUri: Uri?)

object AuthTabConstants {
    const val RESULT_OK = android.app.Activity.RESULT_OK
    const val RESULT_CANCELED = android.app.Activity.RESULT_CANCELED
}