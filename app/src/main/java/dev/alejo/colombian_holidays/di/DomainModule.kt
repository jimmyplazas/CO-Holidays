package dev.alejo.colombian_holidays.di

import dev.alejo.colombian_holidays.data.local.AppPreferencesRepositoryImpl
import dev.alejo.colombian_holidays.data.remote.RepositoryImpl
import dev.alejo.colombian_holidays.domain.repository.AppPreferencesRepository
import dev.alejo.colombian_holidays.domain.repository.Repository
import dev.alejo.colombian_holidays.domain.usecase.ChangeBackgroundUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    single<AppPreferencesRepository> { AppPreferencesRepositoryImpl(get()) }
    singleOf(::ChangeBackgroundUseCase)
    single<Repository>{ RepositoryImpl(get()) }
}