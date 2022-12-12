package com.codinglance.githubtrendingrepo.repository

sealed class ResultState<out T>{
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error<T>(val error: String) : ResultState<T>()
    object Loading: ResultState<Nothing>()
}
