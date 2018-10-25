package com.hamletleon.moviewratings.repositories.remote

import com.hamletleon.moviewratings.BuildConfig
import com.hamletleon.moviewratings.repositories.remote.configurations.ConfigurationsRemoteService
import com.hamletleon.moviewratings.repositories.remote.core.RetrofitClient
import com.hamletleon.moviewratings.repositories.remote.movies.MoviesRemoteService
import retrofit2.Retrofit

object RemoteServiceFactory {
    fun getMoviesRemoteService(): MoviesRemoteService? {
        return getRetrofit()?.create(MoviesRemoteService::class.java)
    }

    fun getConfigurationsRemoteService(): ConfigurationsRemoteService? {
        return getRetrofit()?.create(ConfigurationsRemoteService::class.java)
    }

    private fun getRetrofit(): Retrofit? {
        return RetrofitClient.getClient(BuildConfig.BASE_URL, BuildConfig.API_KEY)
    }
}