package com.example.berlinclock.domain.model

data class BerlinClock(
    val second: Boolean = false,
    val hoursBy5: List<Boolean> = listOf(false, false, false, false),
)
