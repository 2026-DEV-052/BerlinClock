package com.example.berlinclock

import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.model.Time
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import com.example.berlinclock.domain.usecase.GetTimeUseCase
import com.example.berlinclock.presentation.viewmodel.BerlinClockViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
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
    fun `When the viewModel is init, state becomes Content`() {
        every { getTimeUseCase() } returns defaultTime
        every { convertTimeToBerlinClockUseCase(any()) } returns defaultBerlinClock
        val expectedFormatedTime = "00:00:00"

        viewModel.init()
        mainDispatcher.scheduler.runCurrent()

        assertEquals(
            expected = BerlinClockViewModel.State.Content(
                time = defaultTime,
                formattedTime = expectedFormatedTime,
                berlinClock = defaultBerlinClock
            ),
            actual = viewModel.state.value
        )
    }

    @Test
    fun `when time is requested, a Time object is provided`() {
        every { getTimeUseCase() } returns defaultTime

        assertEquals(expected = defaultTime, actual = viewModel.requestCurrentTime())
    }
}
