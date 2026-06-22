package com.example.berlinclock

import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.model.Time
import com.example.berlinclock.domain.repository.TimeRepository
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import com.example.berlinclock.domain.usecase.GetTimeUseCase
import com.example.berlinclock.presentation.viewmodel.BerlinClockViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class BerlinClockViewModelTest {

    lateinit var viewModel: BerlinClockViewModel

    private val defaultTime = Time(hours = 9, minutes = 5, seconds = 3)

    @BeforeTest
    fun init() {
        viewModel = BerlinClockViewModel(
            getTime = GetTimeUseCase(
                timeRepository = object : TimeRepository {
                    override fun getTime() = defaultTime
                }
            ),
            convertTimeToBerlinClock = object : ConvertTimeToBerlinClockUseCase() {
                override fun invoke(hours: Int, minutes: Int, seconds: Int): BerlinClock {
                    return super.invoke(0, 0, 1)
                }
            }
        )
    }

    @Test
    fun `When the viewModel is created, state is Initialized`() {
        assertEquals(expected = BerlinClockViewModel.State.Initialized, viewModel.state.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When the viewModel is init, state is Loading then Content`() = runTest {
        viewModel.init()
        advanceUntilIdle()
        assertEquals(expected = BerlinClockViewModel.State.Loading, viewModel.state.value)
        advanceUntilIdle()
        assertEquals(
            expected = BerlinClockViewModel.State.Content(time = defaultTime, formattedTime = "09:05:03", BerlinClock()),
            actual = viewModel.state.value
        )
    }

    @Test
    fun `when time is requested, a Time object is provided`() {
        assertEquals(expected = defaultTime, actual = viewModel.requestCurrentTime())
    }

}