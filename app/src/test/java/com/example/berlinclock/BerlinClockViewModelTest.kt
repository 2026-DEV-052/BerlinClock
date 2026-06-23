package com.example.berlinclock

import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.model.Time
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import com.example.berlinclock.domain.usecase.GetTimeUseCase
import com.example.berlinclock.presentation.viewmodel.BerlinClockViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BerlinClockViewModelTest {

    private val mainDispatcher = StandardTestDispatcher()

    lateinit var viewModel: BerlinClockViewModel

    @MockK
    lateinit var getTimeUseCase: GetTimeUseCase
    @MockK
    lateinit var convertTimeToBerlinClockUseCase: ConvertTimeToBerlinClockUseCase

    private val defaultTime = Time(hours = 0, minutes = 0, seconds = 0)
    private val defaultBerlinClock = BerlinClock()

    @BeforeTest
    fun init() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainDispatcher)
        viewModel = BerlinClockViewModel(getTimeUseCase, convertTimeToBerlinClockUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When the viewModel is created, state is Initialized`() {
        assertEquals(expected = BerlinClockViewModel.State.Initialized, viewModel.state.value)
    }

    @Test
    fun `state becomes Content once collected`() = runTest {
        every { getTimeUseCase() } returns defaultTime
        every { convertTimeToBerlinClockUseCase(any()) } returns defaultBerlinClock

        viewModel.state.launchIn(backgroundScope)
        mainDispatcher.scheduler.runCurrent()

        assertEquals(
            expected = BerlinClockViewModel.State.Content(
                time = defaultTime,
                formattedTime = "00:00:00",
                berlinClock = defaultBerlinClock
            ),
            actual = viewModel.state.value
        )
    }

    @Test
    fun `state re-emits every second`() = runTest(mainDispatcher) {
        every { getTimeUseCase() } returnsMany listOf(defaultTime, defaultTime)
        every { convertTimeToBerlinClockUseCase(any()) } returns defaultBerlinClock

        viewModel.state.launchIn(backgroundScope)
        mainDispatcher.scheduler.runCurrent()
        mainDispatcher.scheduler.advanceTimeBy(1_000)
        mainDispatcher.scheduler.runCurrent()

        verify(exactly = 2) { getTimeUseCase() }
    }

    @Test
    fun `state becomes Error when getTime throws`() = runTest {
        every { getTimeUseCase() } throws RuntimeException("boom")

        viewModel.state.launchIn(backgroundScope)
        mainDispatcher.scheduler.runCurrent()

        assertEquals(
            expected = BerlinClockViewModel.State.Error("boom"),
            actual = viewModel.state.value
        )
    }
}
