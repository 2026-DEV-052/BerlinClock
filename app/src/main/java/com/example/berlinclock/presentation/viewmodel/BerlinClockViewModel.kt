package com.example.berlinclock.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BerlinClockViewModel(
    val convertTimeToBerlinClock: ConvertTimeToBerlinClockUseCase
) : ViewModel() {
    val _state = MutableStateFlow<State>(State.Initialized)
    val state: StateFlow<State>
        get() = _state

    fun init() {
    }

    sealed class State {
        data object Initialized : State()
        data object Loading : State()
        data class Content(val berlinClock: BerlinClock) : State()
        data class Error(val message: String) : State()
    }
}
