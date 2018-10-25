package com.hamletleon.moviewratings.repositories.remote.configurations

import com.hamletleon.moviewratings.models.apiConfiguration.ApiConfiguration
import retrofit2.Call
import retrofit2.http.GET

interface ConfigurationsRemoteService {
    @GET("configuration")
    fun getApiConfiguration(): Call<ApiConfiguration>
}