package com.hamletleon.moviewratings.ui.movies.list

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamletleon.moviewratings.R
import com.hamletleon.moviewratings.adapter.movies.MovieItemModel
import com.hamletleon.moviewratings.adapter.movies.MoviesAdapter
import com.hamletleon.moviewratings.models.movies.MovieBrief
import com.hamletleon.moviewratings.repositories.MoviesRepository
import com.hamletleon.moviewratings.utils.EndlessRecyclerViewScrollListener
import com.hamletleon.moviewratings.utils.enums.OrderBy
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

class MovieListViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    private val repository: MoviesRepository = MoviesRepository(application)

    private val job = Job()
    override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main

    var adapter: MoviesAdapter? = null

    // Observers
    val movies = mutableListOf<MovieBrief>()
    val filteredMovies = mutableListOf<MovieBrief>()
    val notify = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    val emptyState = MutableLiveData<Boolean>()

    var orderBy = OrderBy.DATE

    private var originalTotalResults = 0
    private var originalPage = 1

    private var totalResults = 0
    private var page = 1
    private var disableScrollListener: Boolean = false
    fun mScrollListener(manager: GridLayoutManager) = object: EndlessRecyclerViewScrollListener(manager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
            if (totalResults > totalItemsCount && !disableScrollListener) {
                this@MovieListViewModel.page = page
                getMoviesBrief(orderBy, page = page, loading = false)
            }
        }
    }

    fun getMoviesBrief(orderBy: OrderBy = OrderBy.DATE, page: Int = 1, loading: Boolean = true) {
        if (loading) this@MovieListViewModel.loading.postValue(true)
        launch {
            val response = repository.getMoviesBriefAsync(orderBy = orderBy, page = page).await()
            if (response.second != null) {
                // TODO -> Show Error
            }
            if (response.first != null) {
                // TODO -> Fill Adapter with local or remote, whatever
                totalResults = response.first?.totalResults ?: 0
                if (response.first?.results != null){
                    if (orderBy == OrderBy.DATE) movies.addAll(response.first?.results!!)
                    else filteredMovies.addAll(response.first?.results!!)
                } else emptyState.postValue(true)
                notify.postValue(true)
            }
            if (loading) this@MovieListViewModel.loading.postValue(false)
        }
    }

    fun saveToFavorite(view: View, item: MovieItemModel) {
        Log.d("TEST", item.title)
        val movieOnList = (if (orderBy == OrderBy.DATE) movies else filteredMovies).find { it.id == item.id } ?: return
        launch {
            if (!item.addedToFavourite) {
                val saved = repository.saveMovieBriefToFavourite(movieOnList)
                if (saved.first) {
                    movieOnList.addedToFavourite = true
                    adapter?.putMovieBrief(movieOnList)
                    notify.postValue(true)
                } else {
                    // TODO -> Show Error
                }
            } else {
                val deleted = repository.deleteMovieBriefFromFavourite(movieOnList)
                if (deleted.first) {
                    movieOnList.addedToFavourite = false
                    adapter?.putMovieBrief(movieOnList)
                    notify.postValue(true)
                } else {
                    // TODO -> Show Error
                }
            }
        }
    }

    fun setOrderBy(id: Int) {
        if (orderBy == OrderBy.DATE && id == R.id.releaseDateFilter) return
        if (orderBy == OrderBy.NAME && id == R.id.nameFilter) return
        if (orderBy == OrderBy.RATING && id == R.id.ratingFilter) return

        orderBy = when (id) {
            R.id.nameFilter -> OrderBy.NAME
            R.id.ratingFilter -> OrderBy.RATING
            else -> OrderBy.DATE
        }
        if (orderBy != OrderBy.DATE) {
            originalTotalResults = totalResults
            originalPage = page
            page = 1
            filteredMovies.clear()
            adapter?.clear()
            getMoviesBrief(orderBy, page, true)
        } else resetList()
    }

    private fun resetList() {
        orderBy = OrderBy.DATE
        totalResults = originalTotalResults
        page = originalPage
        filteredMovies.clear()
        adapter?.clear()
        adapter?.putMovieBriefs(movies, false)
        notify.postValue(true)
    }
}