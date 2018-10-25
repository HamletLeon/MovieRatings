package com.hamletleon.moviewratings.adapter.movies

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.hamletleon.moviewratings.R
import com.hamletleon.moviewratings.models.movies.MovieBrief
import com.hamletleon.moviewratings.utils.models.getMovieItemModel

class MoviesAdapter(private val width: Int? = null, val activity: Activity? = null, private val onItemClicked: (View, MovieItemModel) -> Unit): RecyclerView.Adapter<MovieViewHolder>() {
    private val items = mutableListOf<MovieItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.movie_item_list, parent, false)
        if (width != null && width != 0) {
            val params = view.layoutParams
            params.width = width
            view.layoutParams = params
        }
        return MovieViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = items[position]
        val args = Bundle()
        args.putInt("MovieId", item.id)
        holder.bind(item, onItemClicked)?.setOnClickListener {
            val act = activity ?: (it.rootView.context as? Activity)
            if (act != null) Navigation.findNavController(act, R.id.nav_host_fragment).navigate(R.id.movieDetailsFragment, args)
        }
    }

    fun putMovieBriefs(movies: List<MovieBrief>, notify: Boolean = true) {
        movies.forEach { movie ->  putMovieItem(movie.getMovieItemModel()) }
        if (notify) notifyDataSetChanged()
    }

    fun putMovieBrief(movie: MovieBrief, notify: Boolean = true) = putMovieItem(movie.getMovieItemModel(), notify)

    fun removeMovieBrief(movie: MovieBrief, notify: Boolean = true) = removeMovieItem(movie.getMovieItemModel(), notify)

    private fun putMovieItem(movie: MovieItemModel?, notify: Boolean = true) {
        if (movie == null) return
        val movieExist = this.items.find { it.id == movie.id }
        if (movieExist == null) items.add(movie)
        else {
            val position = this.items.indexOf(movieExist)
            this.items[position] = movie
        }
        if (notify) notifyDataSetChanged()
    }

    private fun removeMovieItem(movie: MovieItemModel?, notify: Boolean = true) {
        if (movie == null) return
        val movieExist = this.items.find { it.id == movie.id }
        if (movieExist != null) {
            val position = this.items.indexOf(movieExist)
            items.removeAt(position)
            if (notify) notifyItemRemoved(position)
        }
    }

    fun clear() {
        items.clear()
    }
}