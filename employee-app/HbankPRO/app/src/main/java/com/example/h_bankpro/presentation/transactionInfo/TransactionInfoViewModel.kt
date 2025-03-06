package com.example.h_bankpro.presentation.transactionInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bankpro.data.utils.NetworkUtils.onFailure
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.domain.useCase.GetOperationInfoUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionInfoViewModel(
    savedStateHandle: SavedStateHandle,
    private val getOperationInfoUseCase: GetOperationInfoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TransactionInfoState())
    val state: StateFlow<TransactionInfoState> = _state

    private val _navigationEvent = MutableSharedFlow<TransactionInfoNavigationEvent>()
    val navigationEvent: SharedFlow<TransactionInfoNavigationEvent> = _navigationEvent

    private val operationId: String = checkNotNull(savedStateHandle["operationId"])

    init {
        _state.update { it.copy(operationId = operationId) }
        loadOperationInfo()
    }

    private fun loadOperationInfo() {
        viewModelScope.launch {
            getOperationInfoUseCase(operationId)
                .onSuccess { result ->
                    _state.update { it.copy(operation = result.data) }
                }
                .onFailure {
                }
        }
    }

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(TransactionInfoNavigationEvent.NavigateBack)
        }
    }
}