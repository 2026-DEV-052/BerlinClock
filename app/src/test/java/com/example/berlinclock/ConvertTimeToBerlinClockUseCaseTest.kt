package com.example.berlinclock

import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConvertTimeToBerlinClockUseCaseTest {

    val useCase = ConvertTimeToBerlinClockUseCase()

    @Test
    fun `seconds led is lit during even seconds `() {
        assertTrue { useCase(hours = 0, minutes = 0, seconds = 2).second }
    }

    @Test
    fun `seconds led is off during odd seconds `() {
        assertFalse { useCase(hours = 0, minutes = 0, seconds = 21).second }
    }
}
