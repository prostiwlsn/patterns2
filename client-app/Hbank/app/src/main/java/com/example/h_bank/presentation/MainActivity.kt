package com.example.h_bank.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import com.example.h_bank.data.ThemeMode
import com.example.h_bank.data.UserSettings
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
import com.example.h_bank.di.settingsModule
import com.example.h_bank.domain.useCase.SettingsUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    private val settingsUseCase: SettingsUseCase by inject()

    @SuppressLint("NewApi", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidLogger()
                androidContext(this@MainActivity)
                modules(appModule)
                modules(settingsModule)
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
            val settings by settingsUseCase.settingsFlow.collectAsState(initial = UserSettings(theme = ThemeMode.LIGHT))
            if (settings.theme == ThemeMode.LIGHT) WindowCompat.getInsetsController(
                window,
                window.decorView
            ).isAppearanceLightStatusBars = true
            else WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = false
            HbankTheme(themeMode = settings.theme) {
                Scaffold { AppNavigation() }

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