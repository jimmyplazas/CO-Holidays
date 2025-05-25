package dev.alejo.colombian_holidays.di

import dev.alejo.colombian_holidays.ui.home.HomeViewModel
import dev.alejo.colombian_holidays.ui.util.ImagesProvider
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    single { ImagesProvider }
    viewModelOf(::HomeViewModel)
}