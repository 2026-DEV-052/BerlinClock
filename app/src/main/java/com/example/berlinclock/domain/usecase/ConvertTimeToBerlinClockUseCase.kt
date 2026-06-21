package com.example.berlinclock.domain.usecase

import com.example.berlinclock.domain.model.BerlinClock

class ConvertTimeToBerlinClockUseCase {
    operator fun invoke(hours: Int, minutes: Int, seconds: Int): BerlinClock {
        return BerlinClock(
            second = seconds % 2 == 0
        )
    }
}
