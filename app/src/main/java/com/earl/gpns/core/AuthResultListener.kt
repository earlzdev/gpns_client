package com.earl.gpns.core

import retrofit2.HttpException

interface AuthResultListener {
    fun <T> authorized(value: T)
    fun unauthorized(e: HttpException)
    fun unknownError(e: Exception)
}