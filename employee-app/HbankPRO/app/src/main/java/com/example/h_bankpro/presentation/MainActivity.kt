package com.example.h_bankpro.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.h_bankpro.di.accountModule
import com.example.h_bankpro.di.appModule
import com.example.h_bankpro.di.authorizationModule
import com.example.h_bankpro.di.networkModule
import com.example.h_bankpro.di.userModule
import com.example.h_bankpro.domain.repository.ITokenStorage
import com.example.h_bankpro.domain.useCase.RefreshTokenUseCase
import com.example.h_bankpro.presentation.navigation.AppNavigation
import com.example.h_bankpro.ui.theme.HbankPROTheme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(networkModule)
            modules(authorizationModule)
            modules(appModule)
            modules(userModule)
            modules(accountModule)
        }
        enableEdgeToEdge()
        setContent {
            HbankPROTheme {
                val tokenStorage: ITokenStorage by inject()
                val refreshTokenUseCase: RefreshTokenUseCase by inject()
                AppNavigation(tokenStorage, refreshTokenUseCase)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HbankPROTheme {
        Greeting("Android")
    }
}