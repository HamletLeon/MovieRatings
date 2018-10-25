package com.hamletleon.moviewratings.models.movies

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Movies")
class MovieBrief {
    @PrimaryKey @SerializedName("id") var id: Int = 0
    @SerializedName("vote_count") var voteCount: Int = 0
    @SerializedName("video") var video: Boolean = false
    @SerializedName("vote_average") var voteAverage: Float = 0.0f
    @SerializedName("title") var title: String = "No especificado"
    @SerializedName("popularity") var popularity: Double = 0.0
    @SerializedName("poster_path") var posterPath: String? = null
    @SerializedName("original_language") var originalLanguage: String = ""
    @SerializedName("original_title") var originalTitle: String = ""
    @SerializedName("backdrop_path") var backdropPath: String? = null
    @SerializedName("adult") var adult: Boolean = false
    @SerializedName("overview") var overview: String = ""
    @SerializedName("release_date") var releaseDate: String = ""

    @Transient @Ignore var addedToFavourite: Boolean = false
//    @SerializedName("genre_ids") var genreIds: List<Int> = emptyList()
}