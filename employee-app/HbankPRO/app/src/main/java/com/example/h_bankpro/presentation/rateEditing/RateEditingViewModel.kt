package com.example.h_bankpro.presentation.rateEditing

import androidx.lifecycle.SavedStateHandle
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.domain.entity.Command
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.domain.useCase.UpdateTariffUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RateEditingViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    savedStateHandle: SavedStateHandle,
    private val updateTariffUseCase: UpdateTariffUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(RateEditingState())
    val state: StateFlow<RateEditingState> = _state

    private val _navigationEvent = MutableSharedFlow<RateEditingNavigationEvent>()
    val navigationEvent: SharedFlow<RateEditingNavigationEvent> = _navigationEvent

    private val rateId: String = checkNotNull(savedStateHandle["rateId"])
    private val name: String = checkNotNull(savedStateHandle["name"])
    private val interestRate: String = checkNotNull(savedStateHandle["interestRate"])
    private val description: String = checkNotNull(savedStateHandle["description"])

    init {
        _state.update {
            it.copy(
                rateId = rateId,
                name = name,
                interestRate = interestRate,
                description = description,
                initialName = name,
                initialInterestRate = interestRate.toDouble(),
                initialDescription = description,
            )
        }
    }

    fun onRateChange(rate: String) {
        val rateValue = rate.toDoubleOrNull()

        if ((rateValue != null && rateValue <= 100) &&
            !(rate.startsWith('0') && rate.length > 1) ||
            rate.isEmpty()
        ) {
            _state.update { it.copy(interestRate = rate) }
            validateFields()
        }
    }

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun onDescriptionChange(description: String) {
        _state.update { it.copy(description = description) }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(RateEditingNavigationEvent.NavigateBack)
        }
    }

    private fun validateFields() {
        val rateValue = _state.value.interestRate?.toDoubleOrNull() ?: 0.0

        _state.update {
            it.copy(
                areFieldsValid = rateValue > 0.0 &&
                        _state.value.name.isNotBlank() &&
                        _state.value.description.isNotBlank() &&
                        !(_state.value.initialDescription == _state.value.description &&
                                _state.value.initialName == _state.value.name &&
                                _state.value.initialInterestRate == rateValue)
            )
        }
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            updateTariffUseCase(
                rateId,
                state.value.name,
                state.value.interestRate?.toDoubleOrNull() ?: 0.0,
                state.value.description,
            ).onSuccess {
                pushCommandUseCase(Command.RefreshMainScreen)
                _navigationEvent.emit(
                    RateEditingNavigationEvent.NavigateToSuccessfulRateEditing
                )
            }
        }
    }
}