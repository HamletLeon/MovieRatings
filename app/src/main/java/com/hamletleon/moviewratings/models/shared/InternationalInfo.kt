package com.hamletleon.moviewratings.models.shared

import com.google.gson.annotations.SerializedName

class InternationalInfo {
    @SerializedName("iso_3166_1") var iso3166_1: String = ""
    var name: String = ""
}