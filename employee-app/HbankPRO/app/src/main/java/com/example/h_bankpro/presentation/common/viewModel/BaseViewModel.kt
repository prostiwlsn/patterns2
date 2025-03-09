package com.example.h_bankpro.presentation.common.viewModel

import androidx.lifecycle.ViewModel
import com.example.h_bankpro.domain.entity.Command
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    abstract val pushCommandUseCase: PushCommandUseCase

    private val emptyHandler = CoroutineExceptionHandler { _, _ -> }

    private val handler = CoroutineExceptionHandler { _, exception ->
        if (exception is NoInternetConnectionException) {
            CoroutineScope(SupervisorJob()).launch(emptyHandler) {
                pushCommandUseCase(Command.NavigateToNoConnection)
            }
        }
    }

    private var viewModelJob = SupervisorJob()
    protected val viewModelScope = CoroutineScope(Main.immediate + viewModelJob + handler)
}

class NoInternetConnectionException : Exception()