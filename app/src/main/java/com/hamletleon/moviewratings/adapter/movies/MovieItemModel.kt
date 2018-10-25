package com.hamletleon.moviewratings.adapter.movies

data class MovieItemModel(val id: Int, val title: String) {
    var overview: String? = null
    var releasedDate: String? = null
    var imageUrl: String? = null
    var big: Boolean = false
    var popularityAverage: Int = 0
    var language: String? = null
    var addedToFavourite: Boolean = false
}