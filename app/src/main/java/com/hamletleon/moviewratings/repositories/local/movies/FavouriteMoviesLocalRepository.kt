package com.hamletleon.moviewratings.repositories.local.movies

import androidx.room.Dao
import androidx.room.Query
import com.hamletleon.moviewratings.models.movies.FavouriteMovie
import com.hamletleon.moviewratings.models.movies.MovieBrief
import com.hamletleon.moviewratings.repositories.local.core.BaseDAO

@Dao
abstract class FavouriteMoviesLocalRepository: BaseDAO<FavouriteMovie>() {
    @Query("SELECT m.* FROM Movies AS m, FavouriteMovie AS f WHERE f.movieId = m.id ORDER BY m.releaseDate")
    abstract fun getAllFavouriteMoviesBriefOrderedByDate(): List<MovieBrief>

    @Query("SELECT m.* FROM Movies AS m, FavouriteMovie AS f WHERE f.movieId = m.id ORDER BY m.voteCount")
    abstract fun getAllFavouriteMoviesBriefOrderedByRating(): List<MovieBrief>

    @Query("SELECT m.* FROM Movies AS m, FavouriteMovie AS f WHERE f.movieId = m.id ORDER BY m.title")
    abstract fun getAllFavouriteMoviesBriefOrderedByName(): List<MovieBrief>

    @Query("SELECT m.* FROM Movies AS m, FavouriteMovie AS f WHERE f.movieId = m.id AND f.movieId = :movieId LIMIT 1")
    abstract fun getFavouriteMovieBriefById(movieId: Int): MovieBrief?

    @Query("SELECT * FROM FavouriteMovie WHERE movieId = :movieId LIMIT 1")
    abstract fun getFavouriteMovieById(movieId: Int): FavouriteMovie?
}