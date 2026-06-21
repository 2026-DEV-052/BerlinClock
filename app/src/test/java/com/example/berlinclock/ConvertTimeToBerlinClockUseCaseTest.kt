package com.example.berlinclock

import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConvertTimeToBerlinClockUseCaseTest {

    val useCase = ConvertTimeToBerlinClockUseCase()

    @Test
    fun `seconds led is lit during even seconds`() {
        assertTrue { useCase(hours = 0, minutes = 0, seconds = 2).second }
    }

    @Test
    fun `seconds led is off during odd seconds`() {
        assertFalse { useCase(hours = 0, minutes = 0, seconds = 21).second }
    }

    @Test
    fun `1 five-hour led lit when the number of hours is less than 10`() {
        val expected = listOf(true, false, false, false)

        assertEquals(
            expected = expected,
            actual = useCase(hours = 7, minutes = 0, seconds = 0).hoursBy5
        )
    }

    @Test
    fun `2 five-hour led lit when the number of hours is less than 15`() {
        val expected = listOf(true, true, false, false)

        assertEquals(
            expected = expected,
            actual = useCase(hours = 12, minutes = 0, seconds = 0).hoursBy5
        )
    }

    @Test
    fun `3 five-hour led lit when the number of hours is less than 20`() {
        val expected = listOf(true, true, true, false)

        assertEquals(
            expected = expected,
            actual = useCase(hours = 19, minutes = 0, seconds = 0).hoursBy5
        )
    }

    @Test
    fun `4 five-hour led lit when the number of hours is equal to 20`() {
        val expected = listOf(true, true, true, true)

        assertEquals(
            expected = expected,
            actual = useCase(hours = 20, minutes = 0, seconds = 0).hoursBy5
        )
    }

    @Test
    fun `all five-hour led are off when the number of hours is less than 5`() {
        val expected = listOf(false, false, false, false)

        assertEquals(
            expected = expected,
            actual = useCase(hours = 3, minutes = 0, seconds = 0).hoursBy5
        )
    }
}
