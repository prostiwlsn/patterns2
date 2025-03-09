package com.example.h_bankpro.presentation.rate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bankpro.data.utils.NetworkUtils.onFailure
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.domain.entity.Command
import com.example.h_bankpro.domain.useCase.DeleteTariffUseCase
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RateViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    savedStateHandle: SavedStateHandle,
    private val deleteTariffUseCase: DeleteTariffUseCase
) : BaseViewModel() {
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
        viewModelScope.launch {
            deleteTariffUseCase(rateId)
                .onSuccess {
                    pushCommandUseCase(Command.RefreshMainScreen)
                    _navigationEvent.emit(
                        RateNavigationEvent.NavigateToSuccessfulRateDeletion
                    )
                }
        }
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