package com.my.domain.model.base

sealed class RequestResult<T>(
    val resultData: T? = null,
    val code: String? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : RequestResult<T>(data)
    class Error<T>(code: String, message: String?) : RequestResult<T>(code = code, message = message)
    class ConnectionError<T>(code: String, message: String?) : RequestResult<T>(code = code, message = message)
    class DataEmpty<T>() : RequestResult<T>()
}