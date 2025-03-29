package com.example.h_bank.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.h_bank.di.accountModule
import com.example.h_bank.di.appModule
import com.example.h_bank.di.authorizationModule
import com.example.h_bank.di.loanModule
import com.example.h_bank.di.logoutModule
import com.example.h_bank.di.operationModule
import com.example.h_bank.di.tokenModule
import com.example.h_bank.di.userModule
import com.example.h_bank.presentation.navigation.AppNavigation
import com.example.h_bank.ui.theme.HbankTheme
import com.example.h_bank.di.networkModule
import com.example.h_bank.di.paymentModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidLogger()
                androidContext(this@MainActivity)
                modules(appModule)
                modules(accountModule)
                modules(authorizationModule)
                modules(networkModule)
                modules(operationModule)
                modules(userModule)
                modules(tokenModule)
                modules(logoutModule)
                modules(loanModule)
                modules(paymentModule)
            }
        }

        handleIntent(intent)

        enableEdgeToEdge()
        setContent {
            HbankTheme {
                AppNavigation()
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.data?.let { uri: Uri ->
            if (uri.scheme == "hbank" && uri.host == "auth") {
                val segments = uri.pathSegments
                if (segments.size >= 2) {
                    val accessToken = segments[0]
                    val refreshToken = segments[1]
                    GlobalScope.launch {
                        AuthEventBus.emitTokens(accessToken, refreshToken)
                    }
                } else {
                }
            } else {
            }
        } ?: { }
    }
}