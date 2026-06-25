package com.example.berlinclock.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.model.Time
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import com.example.berlinclock.domain.usecase.GetTimeUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class BerlinClockViewModel(
    private val getTime: GetTimeUseCase,
    private val convertTimeToBerlinClock: ConvertTimeToBerlinClockUseCase
) : ViewModel() {
    private val mode = MutableStateFlow(ClockMode.DYNAMIC)

    private val staticTime = MutableStateFlow<Time?>(null)

    private val clockTick = flow {
        while (true) {
            emit(Unit)
            delay(1000)
        }
    }

    val state: StateFlow<State> = combine(mode, staticTime, clockTick) { m, frozen, _ ->
        val time = if (m == ClockMode.DYNAMIC) getTime() else frozen
        if (time == null) {
            State.Loading
        } else {
            State.Content(
                time = time,
                formattedTime = time.toFormatedTime(),
                berlinClock = convertTimeToBerlinClock(time),
                mode = m
            )
        }
    }.catch { throwable ->
        emit(State.Error(throwable.message ?: "Unknown error"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.NotInitialized
    )

    fun onDynamicTabClick() {
        mode.value = ClockMode.DYNAMIC
    }

    fun onStaticTabClick() {
        staticTime.value = (state.value as? State.Content)?.time
        mode.value = ClockMode.STATIC
    }

    fun onSubmitStaticTimeClick(time: Time) {
        staticTime.value = time
    }

    private fun Time.toFormatedTime() =
        "${hours.pad()}:${minutes.pad()}:${seconds.pad()}"

    private fun Int.pad() = toString().padStart(2, '0')

    sealed class State {
        data object NotInitialized : State()
        data object Loading : State()
        data class Content(
            val time: Time,
            val formattedTime: String,
            val berlinClock: BerlinClock,
            val mode: ClockMode = ClockMode.DYNAMIC
        ) : State()

        data class Error(val message: String) : State()
    }

    enum class ClockMode { DYNAMIC, STATIC }
}
