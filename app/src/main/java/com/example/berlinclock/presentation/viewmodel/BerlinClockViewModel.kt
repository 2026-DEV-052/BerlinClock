package com.example.berlinclock.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BerlinClockViewModel(
    val convertTimeToBerlinClock: ConvertTimeToBerlinClockUseCase
) : ViewModel() {
    val _state = MutableStateFlow<State>(State.Initialized)
    val state: StateFlow<State>
        get() = _state

    fun init() {
        viewModelScope.launch {
            _state.emit(State.Loading)

            val berlinClock = convertTimeToBerlinClock(
                hours = 10,
                minutes = 35,
                seconds = 59
            )

            _state.emit(State.Content(berlinClock))
        }
    }

    sealed class State {
        data object Initialized : State()
        data object Loading : State()
        data class Content(val berlinClock: BerlinClock) : State()
        data class Error(val message: String) : State()
    }
}
