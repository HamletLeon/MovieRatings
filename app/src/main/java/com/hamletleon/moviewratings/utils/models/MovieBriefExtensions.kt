package com.hamletleon.moviewratings.utils.models

import com.hamletleon.moviewratings.adapter.movies.MovieItemModel
import com.hamletleon.moviewratings.models.movies.MovieBrief

fun MovieBrief?.getMovieItemModel(): MovieItemModel? {
    if (this == null) return null
    val movieItem = MovieItemModel(this.id, this.title)
    movieItem.overview = this.overview
    movieItem.releasedDate = this.releaseDate
    movieItem.imageUrl = this.posterPath ?: this.backdropPath
    movieItem.big = (this.posterPath == null)
    movieItem.popularityAverage = (this.voteAverage * 10).toInt()
    movieItem.language = this.originalLanguage
    movieItem.addedToFavourite = this.addedToFavourite
    return movieItem
}