package com.example.h_bankpro.presentation.rateCreation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RateCreationViewModel : ViewModel() {
    private val _state = MutableStateFlow(RateCreationState())
    val state: StateFlow<RateCreationState> = _state

    private val _navigationEvent = MutableSharedFlow<RateCreationNavigationEvent>()
    val navigationEvent: SharedFlow<RateCreationNavigationEvent> = _navigationEvent

    fun onRateChange(rate: String) {
        val rateValue = rate.toDoubleOrNull()

        if (rateValue != null && rateValue < 100 || rate.isEmpty()) {
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
            _navigationEvent.emit(RateCreationNavigationEvent.NavigateBack)
        }
    }

    private fun validateFields() {
        _state.update {
            it.copy(
                areFieldsValid = _state.value.interestRate.isNotBlank() &&
                        _state.value.name.isNotBlank() &&
                        _state.value.description.isNotBlank()
            )
        }
    }

    fun onCreateClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                RateCreationNavigationEvent.NavigateToSuccessfulRateCreation
            )
        }
    }
}