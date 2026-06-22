package com.example.berlinclock.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.model.Time
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import com.example.berlinclock.domain.usecase.GetTimeUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BerlinClockViewModel(
    val getTime: GetTimeUseCase,
    val convertTimeToBerlinClock: ConvertTimeToBerlinClockUseCase
) : ViewModel() {
    val _state = MutableStateFlow<State>(State.Initialized)
    val state: StateFlow<State>
        get() = _state

    fun init() {
        viewModelScope.launch {
            _state.emit(State.Loading)

            while (isActive) {
                val currentTime = requestCurrentTime()

                val berlinClock = convertTimeToBerlinClock(
                    hours = currentTime.hours,
                    minutes = currentTime.minutes,
                    seconds = currentTime.seconds
                )

                _state.emit(
                    State.Content(
                        currentTime,
                        berlinClock
                    )
                )

                delay(1000)
            }
        }
    }

    fun requestCurrentTime() = getTime()

    sealed class State {
        data object Initialized : State()
        data object Loading : State()
        data class Content(val time: Time, val berlinClock: BerlinClock) : State()
        data class Error(val message: String) : State()
    }
}
