package com.example.h_bank.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.h_bank.di.appModule
import com.example.h_bank.domain.repository.ITokenStorage
import com.example.h_bank.domain.useCase.RefreshTokenUseCase
import com.example.h_bank.presentation.navigation.AppNavigation
import com.example.h_bank.ui.theme.HbankTheme
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule)
        }
        enableEdgeToEdge()
        setContent {
            HbankTheme {
                val tokenStorage: ITokenStorage by inject()
                val refreshTokenUseCase: RefreshTokenUseCase by inject()
                AppNavigation(tokenStorage, refreshTokenUseCase)
            }
        }
    }
}