package com.example.berlinclock.domain.usecase

import com.example.berlinclock.domain.repository.TimeRepository

class GetTimeUseCase(
    val timeRepository: TimeRepository
) {
    operator fun invoke() = timeRepository.getTime()
}