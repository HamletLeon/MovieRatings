package com.hamletleon.moviewratings.utils

import com.hamletleon.moviewratings.models.shared.ApiError
import com.hamletleon.moviewratings.models.shared.Error
import com.hamletleon.moviewratings.repositories.remote.core.parseStringToObject
import retrofit2.Response

fun Response<*>?.getApiError(): Error? {
    if (this == null) return Error(null)
    val error = Error(this.code())
    val errorBody = this.errorBody()?.string()
    val apiError = errorBody.parseStringToObject(ApiError::class.java) ?: ApiError()
    error.apiErrorCode = apiError.code
    error.message = apiError.message ?: "Unknown Error"
    return error
}