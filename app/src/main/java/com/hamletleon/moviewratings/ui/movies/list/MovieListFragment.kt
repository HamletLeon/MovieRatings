package com.hamletleon.moviewratings.ui.movies.list

import android.os.Bundle
import android.util.Log
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
import com.hamletleon.moviewratings.utils.enums.OrderBy
import kotlinx.android.synthetic.main.movie_list_fragment.*

class MovieListFragment: Fragment() {
    private lateinit var viewModel: MovieListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)

        setViewActions()
        setViewListeners()
        setListeners()

        if (viewModel.movies.isEmpty()) viewModel.getMoviesBrief()
        else setAdapterAndView(viewModel.movies)
    }

    private fun setViewActions() {
        val manager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = manager
        val paginater = viewModel.mScrollListener(manager)
        recyclerView.addOnScrollListener(paginater)
        recyclerView.adapter = viewModel.adapter
    }

    private fun setViewListeners() {
        filterGroup.setOnCheckedChangeListener { chip, i ->
            if (chip.checkedChipId <= 0) chip.check(R.id.releaseDateFilter)
            viewModel.setOrderBy(i)
        }
    }

    private fun setListeners() {
        viewModel.notify.observe(this, Observer {
            if (it == true) {
                val movies = if (viewModel.orderBy == OrderBy.DATE) viewModel.movies else viewModel.filteredMovies
                if (viewModel.adapter == null){
                    setAdapterAndView(movies)
                    viewModel.emptyState.postValue(movies.isEmpty())
                } else viewModel.adapter?.putMovieBriefs(movies, true)
            }
        })

        viewModel.loading.observe(this, Observer {
            progressLayout.visibility = if (it == true) View.VISIBLE else View.INVISIBLE
            recyclerView.visibility = if (it != true) View.VISIBLE else View.INVISIBLE
            emptyState.visibility = if (it != true && viewModel.emptyState.value == true) View.VISIBLE else View.INVISIBLE
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