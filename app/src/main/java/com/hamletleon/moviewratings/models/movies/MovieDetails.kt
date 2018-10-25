package com.hamletleon.moviewratings.models.movies

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.hamletleon.moviewratings.models.shared.Genre

@Entity(tableName = "MovieDetails")
class MovieDetails {
    @PrimaryKey var id: Int = 0
    var adult: Boolean = false
    @SerializedName("backdrop_path") var backdropPath: String? = null
    var budget: Int = 0

    var homepage: String? = null
    @SerializedName("imdb_id") var imdbId: String? = null
    @SerializedName("original_language") var originalLanguage: String = ""
    @SerializedName("original_title") var originalTitle: String = ""
    var overview: String? = null
    var popularity: Float = 0f
    @SerializedName("poster_path") var posterPath: String? = null

    @Ignore @SerializedName("genres") var genresList: List<Genre> = emptyList() // I'm not going to save it neither
    var genre: String = ""
//    @TypeConverters(RoomUtils::class) @SerializedName("production_companies") var productionCompanies: List<ProductionCompany> = emptyList()
//    @TypeConverters(RoomUtils::class) @SerializedName("production_countries") var productionCountries: List<InternationalInfo> = emptyList()
//    @TypeConverters(RoomUtils::class) @SerializedName("spoken_languages") var spokenLanguages: List<InternationalInfo> = emptyList()

    @SerializedName("release_date") var releaseDate: String = ""
    var revenue: Int = 0
    var runtime: Int? = null

    var status: String? = MovieStatus.Rumored.value
    var tagline: String? = null
    var title: String = ""
    var video: Boolean = false
    @SerializedName("vote_average") var voteAverage: Float = 0f
    @SerializedName("vote_count") var voteCount: Int = 0

    @Transient @Ignore var addedToFavourite: Boolean = false
}