package com.example.berlinclock.data.repository

import com.example.berlinclock.domain.model.Time
import com.example.berlinclock.domain.repository.TimeRepository
import java.time.LocalTime

class LocalTimeRepositoryImpl : TimeRepository {
    override fun getTime(): Time {
        val localTime = LocalTime.now()
        return Time(
            hours = localTime.hour,
            minutes = localTime.minute,
            seconds = localTime.second,
        )
    }
}