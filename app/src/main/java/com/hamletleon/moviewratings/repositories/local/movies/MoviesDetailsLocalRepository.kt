package com.hamletleon.moviewratings.repositories.local.movies

import androidx.room.Dao
import androidx.room.Query
import com.hamletleon.moviewratings.models.movies.MovieDetails
import com.hamletleon.moviewratings.repositories.local.core.BaseDAO

@Dao
abstract class MoviesDetailsLocalRepository: BaseDAO<MovieDetails>() {
    @Query("SELECT * FROM MovieDetails WHERE id = :movieId")
    abstract fun getDetailsOfMovie(movieId: Int): MovieDetails?
}