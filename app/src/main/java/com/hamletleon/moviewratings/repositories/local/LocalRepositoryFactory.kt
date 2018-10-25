package com.hamletleon.moviewratings.repositories.local

import android.content.Context
import com.hamletleon.moviewratings.repositories.local.configurations.ConfigurationsLocalRepository
import com.hamletleon.moviewratings.repositories.local.core.DatabaseContext
import com.hamletleon.moviewratings.repositories.local.movies.FavouriteMoviesLocalRepository
import com.hamletleon.moviewratings.repositories.local.movies.MoviesBriefLocalRepository
import com.hamletleon.moviewratings.repositories.local.movies.MoviesDetailsLocalRepository

object LocalRepositoryFactory {
    fun getMoviesBriefLocalRepository(ctx: Context): MoviesBriefLocalRepository {
        return DatabaseContext.getInstance(ctx).getMoviesBriefDAO()
    }

    fun getConfigurationsLocalRepository(ctx: Context): ConfigurationsLocalRepository {
        return DatabaseContext.getInstance(ctx).getConfigurationDAO()
    }

    fun getMoviesDetailsLocalRepository(ctx: Context): MoviesDetailsLocalRepository {
        return DatabaseContext.getInstance(ctx).getMoviesDetailsDAO()
    }

    fun getFavouriteMoviesLocalRepository(ctx: Context): FavouriteMoviesLocalRepository {
        return DatabaseContext.getInstance(ctx).getFavouriteMoviesDAO()
    }
}