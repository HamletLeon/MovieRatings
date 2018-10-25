package com.hamletleon.moviewratings.repositories.local.movies

import androidx.room.Dao
import androidx.room.Query
import com.hamletleon.moviewratings.models.movies.MovieBrief
import com.hamletleon.moviewratings.repositories.local.core.BaseDAO

@Dao
abstract class MoviesBriefLocalRepository: BaseDAO<MovieBrief>() {
    @Query("SELECT * FROM Movies ORDER BY DATETIME(releaseDate) DESC LIMIT :limit OFFSET IFNULL(:offset, 0)")
    abstract fun getAllMoviesOrderedByDate(limit: Int = 20, offset: Int? = null): List<MovieBrief>

    @Query("SELECT * FROM Movies ORDER BY originalTitle DESC LIMIT :limit OFFSET IFNULL(:offset, 0)")
    abstract fun getAllMoviesOrderedByName(limit: Int = 20, offset: Int? = null): List<MovieBrief>

    @Query("SELECT * FROM Movies ORDER BY voteCount DESC LIMIT :limit OFFSET IFNULL(:offset, 0)")
    abstract fun getAllMoviesOrderedByRating(limit: Int = 20, offset: Int? = null): List<MovieBrief>
}