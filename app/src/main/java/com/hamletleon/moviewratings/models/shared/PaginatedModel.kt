package com.hamletleon.moviewratings.models.shared

import com.google.gson.annotations.SerializedName

class PaginatedModel<T> {
    var page: Int = 0
    @SerializedName("total_results") var totalResults: Int = 0
    @SerializedName("total_pages") var totalPages: Int = 0
    @SerializedName("results") var results: List<T> = emptyList()

    companion object {
        fun <T> builPaginatedModelFromScratch(results: List<T>, page: Int = 1, totalResults: Int = results.size,
                                              totalPages: Int = if (results.isNotEmpty()) 1 else 0): PaginatedModel<T> {
            val model = PaginatedModel<T>()
            model.results = results
            model.page = page
            model.totalResults = totalResults
            model.totalPages = totalPages
            return model
        }
    }
}