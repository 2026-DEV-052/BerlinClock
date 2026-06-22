package com.example.berlinclock.domain.usecase

import com.example.berlinclock.data.repository.LocalTimeRepositoryImpl
import com.example.berlinclock.domain.repository.TimeRepository

class GetTimeUseCase(
    val timeRepository: TimeRepository = LocalTimeRepositoryImpl()
) {
    operator fun invoke() = timeRepository.getTime()
}