package com.example.h_bankpro.presentation.rateCreation

import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.domain.entity.Command
import com.example.h_bankpro.domain.useCase.CreateTariffUseCase
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.domain.useCase.tariff.GetTariffFlowUseCase
import com.example.h_bankpro.domain.useCase.tariff.UpdateCurrentTariffUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RateCreationViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val createTariffUseCase: CreateTariffUseCase,
    private val updateTariffUseCase: UpdateCurrentTariffUseCase,
    getTariffFlowUseCase: GetTariffFlowUseCase,
) : BaseViewModel() {
    private val _state = MutableStateFlow(RateCreationState())
    val state: StateFlow<RateCreationState> = _state

    private val _navigationEvent = MutableSharedFlow<RateCreationNavigationEvent>()
    val navigationEvent: SharedFlow<RateCreationNavigationEvent> = _navigationEvent

    init {
        getTariffFlowUseCase().onEach { tariff ->
            _state.update {
                it.copy(
                    interestRate = tariff.rate?.toBigDecimal()?.toPlainString(),
                    name = tariff.name.orEmpty(),
                    description = tariff.description.orEmpty(),
                    areFieldsValid = !tariff.name.isNullOrBlank() &&
                            !tariff.description.isNullOrBlank() &&
                            tariff.rate != null
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onRateChange(rate: String) {
        val rateValue = rate.toDoubleOrNull()

        if (rateValue != null && rateValue <= 100 || rate.isEmpty()) {
            updateTariffUseCase { copy(rate = rate) }
        }
    }

    fun onNameChange(name: String) =
        updateTariffUseCase { copy(name = name) }

    fun onDescriptionChange(description: String) =
        updateTariffUseCase { copy(description = description) }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(RateCreationNavigationEvent.NavigateBack)
        }
    }

    fun onCreateClicked() {
        viewModelScope.launch {
            createTariffUseCase(
                state.value.name,
                state.value.interestRate?.toDoubleOrNull() ?: 0.0,
                state.value.description
            ).onSuccess {
                pushCommandUseCase(Command.RefreshMainScreen)
                _navigationEvent.emit(
                    RateCreationNavigationEvent.NavigateToSuccessfulRateCreation
                )
            }
        }
    }
}