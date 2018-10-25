package com.hamletleon.moviewratings.ui.movies.details

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hamletleon.moviewratings.R
import com.hamletleon.moviewratings.models.movies.MovieDetails
import com.hamletleon.moviewratings.models.movies.MovieStatus
import com.hamletleon.moviewratings.utils.getCurrencyString
import com.hamletleon.moviewratings.utils.load
import com.hamletleon.moviewratings.utils.setDateToTextView
import com.hamletleon.moviewratings.utils.setTextView
import kotlinx.android.synthetic.main.argument_details_layout.view.*
import kotlinx.android.synthetic.main.movie_details_fragment.*
import kotlinx.android.synthetic.main.popularity_details_layout.view.*

class MovieDetailsFragment: Fragment() {
    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var arguments: MovieDetailsFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)

        setListeners()

        getArguments().let {
            arguments = MovieDetailsFragmentArgs.fromBundle(it)
            viewModel.getMovieDetails(arguments.movieId)
        }
    }

    private fun setListeners() {
        viewModel.removeViews.observe(this, Observer {
            if (it == true) {
                detailsLayout.removeAllViews()
            }
        })

        viewModel.notifyView.observe(this, Observer {
            if (it == true) {
                val details = viewModel.movieDetails
                topImage.load(details?.backdropPath ?: details?.posterPath, if (details?.backdropPath!= null) "w780" else "w185")
                details?.title.setTextView(topTitle, R.string.title_not_specified)
                setViewChildren(details)
            }
        })

        viewModel.loading.observe(this, Observer {
            progressLayout.visibility = if (it == true) View.VISIBLE else View.INVISIBLE
            mainLayout.visibility = if (it != true) View.VISIBLE else View.INVISIBLE
        })
    }

    private fun setViewChildren(details: MovieDetails?) {
        setPopularityLayout(details)
        setArgumentLayout(details)
//        val featLayout = layoutInflater.inflate(R.layout.featured_credits_details_layout, detailsScrolledLayout, false) -> TODO: Good idea but i'm tired
    }

    @SuppressLint("SetTextI18n")
    private fun setPopularityLayout(details: MovieDetails?) {
        val popLayout = layoutInflater.inflate(R.layout.popularity_details_layout, detailsScrolledLayout, false)

        val popAverage = ((details?.voteAverage?.toInt() ?: 0) * 10)
        popLayout.progressIndicator.progress = popAverage
        popLayout.progressAverage.text = popAverage.toString() + "%"

        details?.releaseDate.setDateToTextView(popLayout.releaseDate, R.string.release_date_not_specified, "dd/MM/yyyy")
        popLayout.votesCount.text = String.format(getString(R.string.no_votes), details?.voteCount ?: 0)

        popLayout.status.text = MovieStatus.values().find { it.value.toLowerCase() == details?.status?.toLowerCase() }?.esValue
        details?.budget.getCurrencyString().setTextView(popLayout.budget, R.string.empty_currency)
        details?.revenue.getCurrencyString().setTextView(popLayout.revenue, R.string.empty_currency)

        detailsLayout.addView(popLayout)
    }

    private fun setArgumentLayout(details: MovieDetails?) {
        val argLayout = layoutInflater.inflate(R.layout.argument_details_layout, detailsScrolledLayout, false)
        details?.overview.setTextView(argLayout.overview, R.string.overview_not_specified)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            argLayout.overview.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
        details?.genre.setTextView(argLayout.genres, R.string.not_specified)
        detailsLayout.addView(argLayout)
    }
}