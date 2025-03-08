package com.example.h_bank.presentation

import android.annotation.SuppressLint
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
            modules(accountModule)
            modules(authorizationModule)
            modules(networkModule)
            modules(operationModule)
            modules(userModule)
            modules(tokenModule)
            modules(logoutModule)
            modules(loanModule)
        }
        enableEdgeToEdge()
        setContent {
            HbankTheme {
                AppNavigation()
            }
        }
    }
}