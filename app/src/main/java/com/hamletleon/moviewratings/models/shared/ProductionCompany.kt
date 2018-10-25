package com.hamletleon.moviewratings.models.shared

import com.google.gson.annotations.SerializedName

class ProductionCompany: Genre() {
    @SerializedName("logo_path") var logoPath: String = ""
    @SerializedName("origin_country") var originCountry: String = ""
}