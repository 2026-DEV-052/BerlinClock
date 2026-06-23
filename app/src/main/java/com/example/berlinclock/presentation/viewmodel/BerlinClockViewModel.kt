package com.example.berlinclock.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.model.Time
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import com.example.berlinclock.domain.usecase.GetTimeUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class BerlinClockViewModel(
    private val getTime: GetTimeUseCase,
    private val convertTimeToBerlinClock: ConvertTimeToBerlinClockUseCase
) : ViewModel() {
    val state: StateFlow<State> = flow {
        emit(State.Loading)
        while (true) {
            val currentTime = getTime()
            emit(
                State.Content(
                    time = currentTime,
                    formattedTime = currentTime.toDisplayString(),
                    berlinClock = convertTimeToBerlinClock(currentTime)
                )
            )
            delay(1000)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Initialized
    )

    private fun Time.toDisplayString() =
        "${hours.pad()}:${minutes.pad()}:${seconds.pad()}"

    private fun Int.pad() = toString().padStart(2, '0')

    sealed class State {
        data object Initialized : State()
        data object Loading : State()
        data class Content(
            val time: Time,
            val formattedTime: String,
            val berlinClock: BerlinClock
        ) : State()

        data class Error(val message: String) : State()
    }
}
