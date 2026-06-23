package com.example.berlinclock.domain.usecase

import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.model.Time

class ConvertTimeToBerlinClockUseCase {
    operator fun invoke(time: Time): BerlinClock {
        return BerlinClock(
            second = time.seconds % 2 == 0,
            hoursBy5 = (time.hours / 5).toLit(4),
            hoursBy1 = (time.hours % 5).toLit(4),
            minutesBy5 = (time.minutes / 5).toLit(11),
            minutesBy1 = (time.minutes % 5).toLit(4),
        )
    }

    private fun Int.toLit(size: Int): List<Boolean> {
        return (1..size).map {
            it <= this
        }
    }
}
