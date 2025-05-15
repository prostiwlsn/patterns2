package com.example.h_bank.presentation.common.viewModelBase

import androidx.lifecycle.ViewModel
import com.example.h_bank.domain.entity.authorization.Command
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    abstract val pushCommandUseCase: PushCommandUseCase

    private val emptyHandler = CoroutineExceptionHandler { _, _ -> }

    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is NoInternetConnectionException -> {
                CoroutineScope(SupervisorJob()).launch(emptyHandler) {
                    pushCommandUseCase(Command.NavigateToNoConnection)
                }
            }

            is ServerErrorException -> {
                CoroutineScope(SupervisorJob()).launch(emptyHandler) {
                    pushCommandUseCase(Command.NavigateToServerError)
                }
            }
        }
    }

    private var viewModelJob = SupervisorJob()
    protected val viewModelScope = CoroutineScope(Main + viewModelJob + handler)
}

class NoInternetConnectionException : Exception()
class ServerErrorException : Exception()