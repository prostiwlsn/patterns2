package com.example.h_bankpro.presentation.main

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.h_bankpro.data.ThemeMode
import com.example.h_bankpro.data.utils.NetworkUtils.onFailure
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.entity.Command
import com.example.h_bankpro.domain.model.Tariff
import com.example.h_bankpro.domain.model.User
import com.example.h_bankpro.domain.useCase.GetCommandUseCase
import com.example.h_bankpro.domain.useCase.GetCurrentUserUseCase
import com.example.h_bankpro.domain.useCase.GetTariffListUseCase
import com.example.h_bankpro.domain.useCase.GetUsersUseCase
import com.example.h_bankpro.domain.useCase.LogoutUseCase
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.domain.useCase.SettingsUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getTariffListUseCase: GetTariffListUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val settingsUseCase: SettingsUseCase,
    getCommandUseCase: GetCommandUseCase,
) : BaseViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    private val _navigationEvent = MutableSharedFlow<MainNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            settingsUseCase.settingsFlow.collect { settings ->
                _state.update {
                    it.copy(
                        themeMode = settings.theme,
                    )
                }
            }
        }
        getCommandUseCase().onEach { command ->
            when (command) {
                is Command.RefreshMainScreen -> onInit()
                else -> Unit
            }
        }.launchIn(viewModelScope)
        onInit()
    }

    private fun onInit() {
        getCurrentUserId()
        loadInitialTariffs()
        setupTariffPager()
    }

    fun showUsersSheet() {
        _state.update { it.copy(isUsersSheetVisible = true) }
    }

    fun hideUsersSheet() {
        _state.update { it.copy(isUsersSheetVisible = false) }
    }

    fun showRatesSheet() {
        _state.update { it.copy(isTariffsSheetVisible = true) }
    }

    fun hideRatesSheet() {
        _state.update { it.copy(isTariffsSheetVisible = false) }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            getUsersUseCase()
                .onSuccess { result ->
                    val filteredUsers = result.data.filter { user ->
                        user.id != state.value.currentUserId
                    }
                    _state.update { it.copy(users = filteredUsers, isLoading = false) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun loadInitialTariffs() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getTariffListUseCase(pageNumber = 1, pageSize = 3)) {
                is RequestResult.Success -> {
                    _state.update { it.copy(isLoading = false, initialTariffs = result.data.items) }
                }

                is RequestResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }

                is RequestResult.NoInternetConnection -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun setupTariffPager() {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { TariffPagingSource(getTariffListUseCase) }
        ).flow.cachedIn(viewModelScope)

        _state.update { it.copy(tariffsFlow = pager) }
    }

    private fun getCurrentUserId() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getCurrentUserUseCase()
                .onSuccess { result ->
                    _state.update { it.copy(currentUserId = result.data.id) }
                    loadUsers()
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun onUserClicked(user: User) {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToUser(user.id))
        }
    }

    fun onTariffClicked(tariff: Tariff) {
        viewModelScope.launch {
            _navigationEvent.emit(
                MainNavigationEvent.NavigateToRate(
                    tariff.id,
                    tariff.name,
                    tariff.ratePercent.toString(),
                    tariff.description
                )
            )
        }
    }

    fun onCreateRateClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToRateCreation)
        }
    }

    fun onCreateUserClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToUserCreation)
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            logoutUseCase()
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _navigationEvent.emit(MainNavigationEvent.NavigateToWelcome)
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val currentTheme = settingsUseCase.settingsFlow.first().theme
            val newTheme = if (currentTheme == ThemeMode.LIGHT) ThemeMode.DARK else ThemeMode.LIGHT
            settingsUseCase.setTheme(newTheme)
        }
    }
}