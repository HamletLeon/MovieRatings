package com.hamletleon.moviewratings.models.shared

import com.google.gson.annotations.SerializedName

class ApiError {
    @SerializedName("status_code") var code: Int? = null
    @SerializedName("status_message") var message: String? = "Unknown Error"
}

data class Error(val code: Int?, var message: String = "Unknown Error", var apiErrorCode: Int? = null)