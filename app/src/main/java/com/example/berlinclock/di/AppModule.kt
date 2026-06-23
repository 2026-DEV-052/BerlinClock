package com.example.berlinclock.di

import com.example.berlinclock.data.repository.LocalTimeRepositoryImpl
import com.example.berlinclock.domain.repository.TimeRepository
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import com.example.berlinclock.domain.usecase.GetTimeUseCase
import com.example.berlinclock.presentation.viewmodel.BerlinClockViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::LocalTimeRepositoryImpl) { bind<TimeRepository>() }
    factoryOf(::GetTimeUseCase)
    factoryOf(::ConvertTimeToBerlinClockUseCase)
    viewModelOf(::BerlinClockViewModel)
}
