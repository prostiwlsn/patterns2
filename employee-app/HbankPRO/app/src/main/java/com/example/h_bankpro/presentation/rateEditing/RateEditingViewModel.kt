package com.example.h_bankpro.presentation.rateEditing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bankpro.data.utils.NetworkUtils.onFailure
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.domain.useCase.UpdateTariffUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RateEditingViewModel(
    savedStateHandle: SavedStateHandle,
    private val updateTariffUseCase: UpdateTariffUseCase
) : ViewModel() {
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
                interestRate = interestRate.toDouble(),
                description = description,
                initialName = name,
                initialInterestRate = interestRate.toDouble(),
                initialDescription = description,
            )
        }
    }

    fun onRateChange(rate: Double) {
        _state.update { it.copy(interestRate = rate) }
        validateFields()
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
        _state.update {
            it.copy(
                areFieldsValid = _state.value.interestRate > 0 &&
                        _state.value.name.isNotBlank() &&
                        _state.value.description.isNotBlank() &&
                        !(_state.value.initialDescription == _state.value.description &&
                                _state.value.initialName == _state.value.name &&
                                _state.value.initialInterestRate == _state.value.interestRate)
            )
        }
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            updateTariffUseCase(
                rateId,
                state.value.name,
                state.value.interestRate,
                state.value.description,
            )
                .onSuccess {
                    _navigationEvent.emit(
                        RateEditingNavigationEvent.NavigateToSuccessfulRateEditing
                    )
                }
                .onFailure {
                }
        }
    }
}