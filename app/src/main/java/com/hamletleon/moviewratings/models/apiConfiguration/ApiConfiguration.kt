package com.hamletleon.moviewratings.models.apiConfiguration

import com.google.gson.annotations.SerializedName

class ApiConfiguration {
    @SerializedName("images") var images: ImageConfiguration? = null
    @SerializedName("change_keys") var changeKeys: List<String>? = emptyList()
}