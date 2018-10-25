package com.hamletleon.moviewratings.ui.movies.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hamletleon.moviewratings.models.movies.MovieDetails
import com.hamletleon.moviewratings.repositories.MoviesRepository
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

class MovieDetailsViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    private val repository: MoviesRepository = MoviesRepository(application)

    private val job = Job()
    override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main

    var movieDetails: MovieDetails? = null

    val removeViews = MutableLiveData<Boolean>()
    val notifyView = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun getMovieDetails(movieId: Int) {
        loading.postValue(true)
        launch {
            val response = repository.getMovieDetails(movieId).await()
            if (response.second != null) {
                // TODO -> Show Error
            }
            if (response.first != null) {
                // TODO -> Fill with local or remote, whatever
                removeViews.postValue(movieDetails != null)
                movieDetails = response.first
                notifyView.postValue(movieDetails != null)
            }
            loading.postValue(false)
        }
    }
}