package com.hamletleon.moviewratings.repositories.remote.movies

import com.hamletleon.moviewratings.models.movies.MovieDetails
import com.hamletleon.moviewratings.models.movies.MovieBrief
import com.hamletleon.moviewratings.models.shared.PaginatedModel
import com.hamletleon.moviewratings.utils.enums.SortBy
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesRemoteService {
    @GET("discover/movie")
    fun getPaginatedMovies(@Query("page") page: Int, @Query("sort_by") sortBy: String = SortBy.DATE.value,
                           @Query("language") language: String? = "es-ES"): Deferred<Response<PaginatedModel<MovieBrief>?>>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: Int, @Query("language") language: String? = "es-ES",
                        @Query("append_to_response") includeAlso: String? = "credits"): Deferred<Response<MovieDetails?>>
}