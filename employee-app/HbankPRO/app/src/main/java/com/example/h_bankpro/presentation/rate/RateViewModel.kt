package com.example.h_bankpro.presentation.rate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RateViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(RateState())
    val state: StateFlow<RateState> = _state

    private val _navigationEvent = MutableSharedFlow<RateNavigationEvent>()
    val navigationEvent: SharedFlow<RateNavigationEvent> = _navigationEvent

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
                description = description
            )
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(RateNavigationEvent.NavigateBack)
        }
    }

    fun onDeleteClicked() {
    }

    fun onEditClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                RateNavigationEvent.NavigateToRateEditing(
                    rateId,
                    name,
                    interestRate,
                    description
                )
            )
        }
    }
}