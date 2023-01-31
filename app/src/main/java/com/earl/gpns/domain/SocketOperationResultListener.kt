package com.earl.gpns.domain

sealed class SocketOperationResultListener<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): SocketOperationResultListener<T>(data)
    class Error<T>(message: String, data: T? = null): SocketOperationResultListener<T>(data, message)
}
