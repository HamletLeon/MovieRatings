package com.hamletleon.moviewratings.repositories.remote.core

import com.hamletleon.moviewratings.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private lateinit var mHttpClient: OkHttpClient
    private var mRetrofit: Retrofit? = null

    fun getClient(baseUrl: String, apiKey: String): Retrofit? {
        if (mRetrofit == null){
            mHttpClient = getHttpClient(apiKey)
            mRetrofit = getRetrofit(baseUrl)
        }
        return mRetrofit
    }

    private fun getHttpClient(apiKey: String): OkHttpClient{
        val httpClient = OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
        httpClient.addInterceptor {
            val original = it.request()
            val originalUrl = original.url()
            val url = originalUrl.newBuilder()
                    .addQueryParameter("api_key", apiKey)
                    .build()
            val request = original.newBuilder()
                    .url(url).build()
            it.proceed(request)
        }

        // Debug Interceptor
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(interceptor)
        }

        return httpClient.build()
    }

    private fun getRetrofit(baseUrl: String): Retrofit{
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(mHttpClient)
                .build()
    }
}