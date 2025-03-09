package com.example.h_bank.presentation.transactionInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.domain.entity.filter.toDomain
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.domain.useCase.payment.GetOperationDetailsUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import com.example.h_bank.presentation.paymentHistory.utils.DateFormatter.toLocalDateTime
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionInfoViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val getOperationDetailsUseCase: GetOperationDetailsUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    private val _state = MutableStateFlow(TransactionInfoState())
    val state: StateFlow<TransactionInfoState> = _state

    private val _navigationEvent = MutableSharedFlow<TransactionInfoNavigationEvent>()
    val navigationEvent: SharedFlow<TransactionInfoNavigationEvent> = _navigationEvent

    private val transactionId: String = checkNotNull(savedStateHandle["transactionId"])

    init {
        viewModelScope.launch {
            getOperationDetailsUseCase(transactionId)
                .onSuccess { details ->
                    _state.update {
                        it.copy(
                            senderAccount = details.data.senderAccountNumber,
                            recipientAccount = details.data.recipientAccountNumber,
                            comment = details.data.message,
                            amount = details.data.amount.toString(),
                            date = details.data.transactionDateTime.toLocalDateTime(),
                            operationType = details.data.operationType.toDomain(),
                            isLoading = false,
                        )
                    }
                }
        }
    }

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(TransactionInfoNavigationEvent.NavigateBack)
        }
    }
}