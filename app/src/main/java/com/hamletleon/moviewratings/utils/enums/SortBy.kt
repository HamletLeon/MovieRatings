package com.hamletleon.moviewratings.utils.enums

enum class SortBy(val value: String) {
    DATE("release_date.desc"),
    RATING("popularity.desc"),
    NAME("original_title.desc")
}