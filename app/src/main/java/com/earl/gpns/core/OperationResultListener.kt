package com.earl.gpns.core

interface OperationResultListener {
    fun <T> success(value: T)
    fun fail(e: Exception)
}