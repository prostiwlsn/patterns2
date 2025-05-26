package com.example.h_bankpro.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.SavedStateHandle
import com.example.h_bankpro.data.FirebasePushManager
import com.example.h_bankpro.presentation.account.AccountViewModel
import com.example.h_bankpro.presentation.loan.LoanViewModel
import com.example.h_bankpro.presentation.main.MainViewModel
import com.example.h_bankpro.presentation.rate.RateViewModel
import com.example.h_bankpro.presentation.rateEditing.RateEditingViewModel
import com.example.h_bankpro.presentation.successfulRateCreation.SuccessfulRateCreationViewModel
import com.example.h_bankpro.presentation.successfulRateEditing.SuccessfulRateEditingViewModel
import com.example.h_bankpro.presentation.successfulUserCreation.SuccessfulUserCreationViewModel
import com.example.h_bankpro.presentation.operationInfo.OperationInfoViewModel
import com.example.h_bankpro.presentation.serverError.ServerErrorViewModel
import com.example.h_bankpro.presentation.successfulRateDeletion.SuccessfulRateDeletionViewModel
import com.example.h_bankpro.presentation.user.UserViewModel
import com.example.h_bankpro.presentation.userCreation.UserCreationViewModel
import com.example.h_bankpro.presentation.welcome.WelcomeViewModel
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val appModule = module {
    single<DataStore<Preferences>> { androidContext().dataStore }
    viewModel { WelcomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SuccessfulRateCreationViewModel(get()) }
    viewModel { SuccessfulRateEditingViewModel(get()) }
    viewModel { SuccessfulRateDeletionViewModel(get()) }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        RateViewModel(get(), savedStateHandle, get())
    }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        RateEditingViewModel(get(), savedStateHandle, get())
    }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        LoanViewModel(get(), get(), savedStateHandle)
    }
    viewModel { UserCreationViewModel(get(), get()) }
    viewModel { SuccessfulUserCreationViewModel(get()) }
    viewModel { AccountViewModel(get(), get(), get()) }
    viewModel { OperationInfoViewModel(get(), get(), get()) }
    viewModel { (savedStateHandle: SavedStateHandle) ->
        UserViewModel(get(), savedStateHandle, get(), get(), get(), get(), get(), get())
    }
    viewModel { ServerErrorViewModel() }
    single { FirebaseMessaging.getInstance() }
    single { FirebasePushManager(get()) }
}
