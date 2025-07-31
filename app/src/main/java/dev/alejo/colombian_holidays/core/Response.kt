package dev.alejo.colombian_holidays.core

sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(
        val message: String = "",
        val data: Any? = null
    ) : Response<Nothing>()
}