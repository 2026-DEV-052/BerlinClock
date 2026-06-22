package com.example.berlinclock.domain.repository

import com.example.berlinclock.domain.model.Time

interface TimeRepository {
    fun getTime(): Time
}