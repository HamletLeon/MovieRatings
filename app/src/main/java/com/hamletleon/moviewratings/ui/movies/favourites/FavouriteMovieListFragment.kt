package com.hamletleon.moviewratings.ui.movies.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.hamletleon.moviewratings.R
import com.hamletleon.moviewratings.adapter.movies.MoviesAdapter
import com.hamletleon.moviewratings.models.movies.MovieBrief
import kotlinx.android.synthetic.main.favourite_movie_list_fragment.*

class FavouriteMovieListFragment: Fragment() {
    private lateinit var viewModel: FavouriteMovieListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favourite_movie_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavouriteMovieListViewModel::class.java)

        setViewActions()
        setListeners()

        if (viewModel.movies.isEmpty()) viewModel.getFavouriteMoviesBrief()
        else setAdapterAndView(viewModel.movies)
    }

    private fun setViewActions() {
        val manager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = manager
    }

    private fun setListeners() {
        viewModel.notify.observe(this, Observer {
            if (it == true) {
                val movies = viewModel.movies
                if (viewModel.adapter == null){
                    setAdapterAndView(movies)
                } else viewModel.adapter?.putMovieBriefs(movies, true)
                viewModel.emptyState.postValue(movies.isEmpty())
            }
        })

        viewModel.loading.observe(this, Observer {
            progressLayout.visibility = if (it == true) View.VISIBLE else View.INVISIBLE
            recyclerView.visibility = if (it != true) View.VISIBLE else View.INVISIBLE
        })

        viewModel.emptyState.observe(this, Observer {
            progressLayout.visibility = if (it != true && viewModel.loading.value == true) View.VISIBLE else View.INVISIBLE
            recyclerView.visibility = if (it != true) View.VISIBLE else View.INVISIBLE
            emptyState.visibility = if (it == true) View.VISIBLE else View.INVISIBLE
        })
    }

    private fun setAdapterAndView(movies: List<MovieBrief>) {
        viewModel.adapter = MoviesAdapter(activity = requireActivity(), onItemClicked = viewModel::saveToFavorite)
        viewModel.adapter?.putMovieBriefs(movies, false)
        recyclerView.adapter = viewModel.adapter
    }
}