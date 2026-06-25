package com.example.berlinclock

import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.model.Time
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConvertTimeToBerlinClockUseCaseTest {

    val useCase = ConvertTimeToBerlinClockUseCase()

    @Test
    fun `seconds led is lit during even seconds`() {
        assertTrue { useCase(Time(hours = 0, minutes = 0, seconds = 2)).second }
    }

    @Test
    fun `seconds led is off during odd seconds`() {
        assertFalse { useCase(Time(hours = 0, minutes = 0, seconds = 21)).second }
    }

    @ParameterizedTest(name = ": {0} hours {1} five-hour led turn on")
    @CsvSource(
        "4,0",
        "0,0",
        "5,1",
        "9,1",
        "10,2",
        "14,2",
        "15,3",
        "19,3",
        "20,4",
        "21,4",
        "22,4",
        "23,4",
    )
    fun `five hour row lights one led per five hours`(hours: Int, expected: Int) {
        val expected = List(4) { it < expected }

        assertEquals(
            expected = expected,
            actual = useCase(Time(hours = hours, minutes = 0, seconds = 0)).hoursBy5
        )
    }

    @ParameterizedTest(name = ": {0} hours {1} single hour led turn on")
    @CsvSource(
        "0,0",
        "10,0",
        "1,1",
        "6,1",
        "21,1",
        "2,2",
        "7,2",
        "22,2",
        "3,3",
        "23,3",
        "8,3",
        "4,4",
        "9,4",
    )
    fun `one hour row lights one led per hour`(hours: Int, expected: Int) {
        val expected = List(4) { it < expected }

        assertEquals(
            expected = expected,
            actual = useCase(Time(hours = hours, minutes = 0, seconds = 0)).hoursBy1
        )
    }

    @ParameterizedTest(name = ": {0} minutes {1} five minute led turn on")
    @CsvSource(
        "0, 0",
        "5, 1",
        "10, 2",
        "14, 2",
        "15, 3",
        "19, 3",
        "20, 4",
        "45, 9",
        "55, 11",
        "59, 11",
    )
    fun `five minute row lights one led per five minutes`(minutes: Int, expected: Int) {
        val expected = List(11) { it < expected }

        assertEquals(
            expected = expected,
            actual = useCase(Time(hours = 0, minutes = minutes, seconds = 0)).minutesBy5,
        )
    }

    @ParameterizedTest(name = ": {0} minutes {1} single minute led turn on")
    @CsvSource(
        "1,1",
        "2,2",
        "3,3",
        "4,4",
        "5,0",
        "0,0",
    )
    fun `one minute row lights one led per minute`(minutes: Int, expected: Int) {
        val expected = List(4) { it < expected }

        assertEquals(
            expected = expected,
            actual = useCase(Time(hours = 0, minutes = minutes, seconds = 0)).minutesBy1
        )
    }

    @Test
    fun `given a full time and receive a fully configured berlin clock`() {
        val clock = useCase(Time(hours = 13, minutes = 17, seconds = 1))
        assertEquals(
            expected = BerlinClock(
                second = false,
                hoursBy5 = listOf(true, true, false, false),
                hoursBy1 = listOf(true, true, true, false),
                minutesBy5 = listOf(
                    true, true, true,
                    false, false, false, false, false, false, false, false,
                ),
                minutesBy1 = listOf(true, true, false, false),
            ),
            actual = clock
        )
    }
}
