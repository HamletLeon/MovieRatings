package com.hamletleon.moviewratings.models.movies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FavouriteMovie {
    @PrimaryKey var movieId: Int = 0
    var addedAt: String? = ""
}