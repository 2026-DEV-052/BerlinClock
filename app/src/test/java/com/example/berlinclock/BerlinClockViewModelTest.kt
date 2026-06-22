package com.example.berlinclock

import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import com.example.berlinclock.presentation.viewmodel.BerlinClockViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class BerlinClockViewModelTest {

    lateinit var viewModel: BerlinClockViewModel

    @BeforeTest
    fun init() {
        viewModel = BerlinClockViewModel(
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
            expected = BerlinClockViewModel.State.Content(BerlinClock()),
            viewModel.state.value
        )
    }
}