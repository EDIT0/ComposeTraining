package com.my.book.library.core.model.network

sealed class RequestResult<T>(
    val resultData: T? = null,
    val code: Int? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : RequestResult<T>(resultData = data)
    class Error<T>(code: Int, message: String?) : RequestResult<T>(code = code, message = message)
    class AuthError<T>(code: Int, message: String?) : RequestResult<T>(code = code, message = message)
    class ConnectionError<T>(code: Int? = null, message: String? = null) : RequestResult<T>(code = code, message = message)
    class DataEmpty<T> : RequestResult<T>()
    class Loading<T> : RequestResult<T>()
}
