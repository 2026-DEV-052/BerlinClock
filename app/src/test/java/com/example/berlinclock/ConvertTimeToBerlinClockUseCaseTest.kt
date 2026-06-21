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

    @Test
    fun `one hour led lit when the number of hours is remaining is 1`() {
        val expected = listOf(true, false, false, false)

        assertEquals(
            expected = expected,
            actual = useCase(hours = 6, minutes = 0, seconds = 0).hoursBy1
        )
    }

    @Test
    fun `two single hour led lit when the number of hours is remaining is 2`() {
        val expected = listOf(true, true, false, false)

        assertEquals(
            expected = expected,
            actual = useCase(hours = 12, minutes = 0, seconds = 0).hoursBy1
        )
    }

    @Test
    fun `three single hour led lit when the number of hours is remaining is 3`() {
        val expected = listOf(true, true, true, false)

        assertEquals(
            expected = expected,
            actual = useCase(hours = 23, minutes = 0, seconds = 0).hoursBy1
        )
    }

    @Test
    fun `four single hour led lit when the number of hours is remaining is 4`() {
        val expected = listOf(true, true, true, true)

        assertEquals(
            expected = expected,
            actual = useCase(hours = 24, minutes = 0, seconds = 0).hoursBy1
        )
    }

    @Test
    fun `all single hour led are off when the number of hours is remaining is 0`() {
        val expected = listOf(false, false, false, false)

        assertEquals(
            expected = expected,
            actual = useCase(hours = 10, minutes = 0, seconds = 0).hoursBy1
        )
    }

}
