package com.hamletleon.moviewratings.models.apiConfiguration

import com.google.gson.annotations.SerializedName

class ImageConfiguration{
    @SerializedName("base_url") var httpBaseUrl: String? = null
    @SerializedName("secure_base_url") var httpsBaseUrl: String? = null
    @SerializedName("backdrop_sizes") var backdropSizes: List<String>? = emptyList()
    @SerializedName("logo_sizes") var logoSizes: List<String>? = emptyList()
    @SerializedName("poster_sizes") var posterSizes: List<String>? = emptyList()
    @SerializedName("profile_sizes") var profileSizes: List<String>? = emptyList()
    @SerializedName("still_sizes") var stillSizes: List<String>? = emptyList()
}