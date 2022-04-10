package com.mymusic.util

sealed class Task<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Init<T> : Task<T>()
    class Running<T> : Task<T>()
    class Success<T>(data: T? = null) : Task<T>(data = data)
    class Failed<T>(message: String) : Task<T>(message = message)
}