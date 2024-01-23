package com.matrix.android_104_android.resource

sealed class Resource<out T: Any> {
    data class Success<out T: Any>(val data: T): Resource<T>()
    data class Error(val exception: Throwable): Resource<Nothing>()
    object Loading: Resource<Nothing>()
}