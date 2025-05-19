package dev.alejo.colombian_holidays.di

import dev.alejo.colombian_holidays.ui.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::HomeViewModel)
}