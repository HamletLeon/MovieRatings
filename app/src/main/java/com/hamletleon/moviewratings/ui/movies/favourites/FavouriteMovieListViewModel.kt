package com.hamletleon.moviewratings.ui.movies.favourites

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hamletleon.moviewratings.adapter.movies.MovieItemModel
import com.hamletleon.moviewratings.adapter.movies.MoviesAdapter
import com.hamletleon.moviewratings.models.movies.MovieBrief
import com.hamletleon.moviewratings.repositories.MoviesRepository
import com.hamletleon.moviewratings.utils.enums.OrderBy
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

class FavouriteMovieListViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    private val repository: MoviesRepository = MoviesRepository(application)

    private val job = Job()
    override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main

    var adapter: MoviesAdapter? = null

    // Observers
    val movies = mutableListOf<MovieBrief>()
    val notify = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    val emptyState = MutableLiveData<Boolean>()

    fun getFavouriteMoviesBrief(orderBy: OrderBy = OrderBy.DATE) {
        loading.postValue(true)
        launch {
            val response = repository.getMovieBriefsFromFavourite(orderBy.int).await()
            if (response.second != null) {
                // TODO -> Show Error
            } else {
                // TODO -> Fill Adapter with local or remote, whatever
                if (!response.first.isEmpty()) {
                    movies.addAll(response.first)
                    notify.postValue(true)
                } else emptyState.postValue(true)
            }
            loading.postValue(false)
        }
    }

    fun saveToFavorite(view: View, item: MovieItemModel) {
        Log.d("TEST", item.title)
        val movieOnList = movies.find { it.id == item.id } ?: return
        launch {
            if (item.addedToFavourite) {
                val deleted = repository.deleteMovieBriefFromFavourite(movieOnList)
                if (deleted.first) {
                    movies.remove(movieOnList)
                    adapter?.removeMovieBrief(movieOnList, false)
                    notify.postValue(true)
                } else {
                    // TODO -> Show Error
                }
            }
        }
    }
}