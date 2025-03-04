package com.example.h_bankpro.presentation.rateEditing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RateEditingViewModel : ViewModel() {
    private val _state = MutableStateFlow(RateEditingState())
    val state: StateFlow<RateEditingState> = _state

    private val _navigationEvent = MutableSharedFlow<RateEditingNavigationEvent>()
    val navigationEvent: SharedFlow<RateEditingNavigationEvent> = _navigationEvent

    init {
        _state.update {
            it.copy(
                name = _state.value.rate.name,
                interestRate = _state.value.rate.interestRate,
                description = _state.value.rate.description
            )
        }
    }

    fun onRateChange(rate: Float) {
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
                        !(_state.value.description == _state.value.rate.description &&
                                _state.value.name == _state.value.rate.name &&
                                _state.value.interestRate == _state.value.rate.interestRate)
            )
        }
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                RateEditingNavigationEvent.NavigateToSuccessfulRateEditing
            )
        }
    }
}