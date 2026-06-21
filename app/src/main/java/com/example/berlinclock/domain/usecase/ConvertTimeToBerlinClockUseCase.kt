package com.example.berlinclock.domain.usecase

import com.example.berlinclock.domain.model.BerlinClock

class ConvertTimeToBerlinClockUseCase {
    operator fun invoke(hours: Int, minutes: Int, seconds: Int): BerlinClock {
        return BerlinClock(
            second = seconds % 2 == 0,
            hoursBy5 = (hours / 5).toLit(4),
            hoursBy1 = (hours % 5).toLit(4),
            minutesBy5 = (minutes / 5).toLit(11)
        )
    }

    private fun Int.toLit(size: Int): List<Boolean> {
        return (1..size).map {
            it <= this
        }
    }
}
