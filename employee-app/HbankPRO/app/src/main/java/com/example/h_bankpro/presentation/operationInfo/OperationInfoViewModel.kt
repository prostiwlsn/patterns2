package com.example.h_bankpro.presentation.operationInfo

import androidx.lifecycle.SavedStateHandle
import com.example.h_bankpro.data.OperationType
import com.example.h_bankpro.data.utils.NetworkUtils.onFailure
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.domain.useCase.GetOperationInfoUseCase
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class OperationInfoViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    savedStateHandle: SavedStateHandle,
    private val getOperationInfoUseCase: GetOperationInfoUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(OperationInfoState())
    val state: StateFlow<OperationInfoState> = _state

    private val _navigationEvent = MutableSharedFlow<OperationInfoNavigationEvent>()
    val navigationEvent: SharedFlow<OperationInfoNavigationEvent> = _navigationEvent

    private val accountId: String = checkNotNull(savedStateHandle["accountId"])
    private val operationId: String = checkNotNull(savedStateHandle["operationId"])

    private val dateFormatter = DateTimeFormatter.ofPattern(
        "dd.MM.yyyy",
        Locale("ru")
    )
    private val timeFormatter = DateTimeFormatter.ofPattern(
        "HH:mm",
        Locale("ru")
    )

    init {
        loadOperationInfo()
    }

    private fun loadOperationInfo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getOperationInfoUseCase(accountId, operationId)
                .onSuccess { result ->
                    val operation = result.data
                    val displayTitle = when {
                        operation.operationType == OperationType.TRANSFER -> {
                            if (operation.directionToMe) "Входящий перевод"
                            else "Исходящий перевод"
                        }

                        else -> operation.operationType.displayName
                    }
                    val formattedAmount = "${operation.amount} ₽"
                    val formattedDateTime = operation.transactionDateTime.toJavaLocalDateTime()
                        .let { "${it.format(dateFormatter)}, ${it.format(timeFormatter)}" }
                    _state.update {
                        it.copy(
                            operation = operation,
                            displayTitle = displayTitle,
                            formattedAmount = formattedAmount,
                            formattedDateTime = formattedDateTime,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(OperationInfoNavigationEvent.NavigateBack)
        }
    }
}