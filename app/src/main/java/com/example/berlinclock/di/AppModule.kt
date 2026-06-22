package com.example.berlinclock.di

import com.example.berlinclock.data.repository.LocalTimeRepositoryImpl
import com.example.berlinclock.domain.repository.TimeRepository
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import com.example.berlinclock.domain.usecase.GetTimeUseCase
import com.example.berlinclock.presentation.viewmodel.BerlinClockViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<TimeRepository> { LocalTimeRepositoryImpl() }
    factory { GetTimeUseCase(get()) }
    factory { ConvertTimeToBerlinClockUseCase() }
    viewModel { BerlinClockViewModel(get(), get()) }
}
