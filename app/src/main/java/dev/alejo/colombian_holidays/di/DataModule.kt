package dev.alejo.colombian_holidays.di

import android.content.Context
import dev.alejo.colombian_holidays.data.remote.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val BASE_URL = "https://date.nager.at/api/v3/"

val dataModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json( json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any )
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KtorLog: $message")
                    }
                }
                level = LogLevel.ALL
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BASE_URL
                }
            }
        }
    }
    single { androidContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    single { ApiService(get()) }
}