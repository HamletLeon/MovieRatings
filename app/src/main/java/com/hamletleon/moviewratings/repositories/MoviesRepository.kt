package com.hamletleon.moviewratings.repositories

import android.content.Context
import com.hamletleon.moviewratings.models.movies.FavouriteMovie
import com.hamletleon.moviewratings.models.movies.MovieBrief
import com.hamletleon.moviewratings.models.movies.MovieDetails
import com.hamletleon.moviewratings.models.shared.Error
import com.hamletleon.moviewratings.models.shared.PaginatedModel
import com.hamletleon.moviewratings.repositories.local.LocalRepositoryFactory
import com.hamletleon.moviewratings.repositories.local.movies.FavouriteMoviesLocalRepository
import com.hamletleon.moviewratings.repositories.local.movies.MoviesBriefLocalRepository
import com.hamletleon.moviewratings.repositories.local.movies.MoviesDetailsLocalRepository
import com.hamletleon.moviewratings.repositories.remote.RemoteServiceFactory
import com.hamletleon.moviewratings.repositories.remote.movies.MoviesRemoteService
import com.hamletleon.moviewratings.utils.enums.OrderBy
import com.hamletleon.moviewratings.utils.enums.SortBy
import com.hamletleon.moviewratings.utils.fromDate
import com.hamletleon.moviewratings.utils.getApiError
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext
import retrofit2.HttpException
import java.util.*

class MoviesRepository(ctx: Context) {
    // Local Repositories
    private val localMoviesRepository: MoviesBriefLocalRepository = LocalRepositoryFactory.getMoviesBriefLocalRepository(ctx)
    private val localMoviesDetailsRepository: MoviesDetailsLocalRepository = LocalRepositoryFactory.getMoviesDetailsLocalRepository(ctx)
    private val localFavouriteMoviesRepository: FavouriteMoviesLocalRepository = LocalRepositoryFactory.getFavouriteMoviesLocalRepository(ctx)

    // Remote Services
    private val remoteMoviesService: MoviesRemoteService? = RemoteServiceFactory.getMoviesRemoteService()

    suspend fun getMoviesBriefAsync(orderBy: OrderBy = OrderBy.DATE, page: Int = 1): Deferred<Pair<PaginatedModel<MovieBrief>?, Error?>> {
        return withContext(Dispatchers.IO) {
            val localMovies = PaginatedModel.builPaginatedModelFromScratch(getMoviesBriefByOrder(orderBy.int, page = page))
            localMovies.results.forEach { it.addedToFavourite = localFavouriteMoviesRepository.getFavouriteMovieBriefById(it.id) != null }
            try {
                // I prefer a request to check last modified date of movies before a full fetch (I use that pattern in my applications)/ HL
                val response = remoteMoviesService?.getPaginatedMovies(page, sortBy = when(orderBy){
                    OrderBy.RATING -> SortBy.RATING.value
                    OrderBy.NAME -> SortBy.NAME.value
                    else -> SortBy.DATE.value
                })?.await()
                if (response?.isSuccessful == true) {
                    val res = response.body()
                    localMoviesRepository.upsert(res?.results)
                    res?.results?.forEach { it.addedToFavourite = localFavouriteMoviesRepository.getFavouriteMovieBriefById(it.id) != null }
                    async { Pair(res, null) }
                } else async { Pair(localMovies, response.getApiError()) }
            } catch (e: Exception) {
                if (e is HttpException) async { Pair(localMovies, Error(e.code(), e.message())) }
                else async { Pair(localMovies, Error(null, e.localizedMessage)) }
            }
        }
    }

    suspend fun saveMovieBriefToFavourite(movie: MovieBrief): Pair<Boolean, Error?> {
        return withContext(Dispatchers.IO) {
            val favourite = FavouriteMovie()
            favourite.addedAt = Date().fromDate() ?: Calendar.getInstance().time.fromDate()
            favourite.movieId = movie.id
            val res = localFavouriteMoviesRepository.insert(favourite)
Pair(res > -1, if (res <= -1) Error(null, "No se pudo guardar en la base de datos.") else null)
        }
    }

    suspend fun deleteMovieBriefFromFavourite(movie: MovieBrief): Pair<Boolean, Error?> {
        return withContext(Dispatchers.IO) {
            val favourite = localFavouriteMoviesRepository.getFavouriteMovieById(movie.id)
            if (favourite != null) {
                localFavouriteMoviesRepository.delete(favourite)
                Pair(true, null)
            } else Pair(false, Error(null, "No se pudo eliminar de la base de datos."))
        }
    }

    suspend fun getMovieBriefsFromFavourite(orderBy: Int): Deferred<Pair<List<MovieBrief>, Error?>> {
        return withContext(Dispatchers.IO) {
            val favouriteMovies = getFavouriteMoviesBriefByOrder(orderBy)
            favouriteMovies.forEach { it.addedToFavourite = true }
            async { Pair(favouriteMovies, null) }
        }
    }

    suspend fun getMovieDetails(movieId: Int): Deferred<Pair<MovieDetails?, Error?>> {
        return withContext(Dispatchers.IO) {
            val localMovieDetails = localMoviesDetailsRepository.getDetailsOfMovie(movieId)
            localMovieDetails?.addedToFavourite = localFavouriteMoviesRepository.getFavouriteMovieById(movieId) != null
            try {
                if (localMovieDetails != null) return@withContext async { Pair(localMovieDetails, null) }
                // I prefer a request to check last modified date of movie details before a full fetch (I use that pattern in my applications)/ HL
                val response = remoteMoviesService?.getMovieDetails(movieId)?.await()
                if (response?.isSuccessful == true) {
                    val res = response.body()
                    res?.genre = res?.genresList?.joinToString { it.name } ?: ""
                    localMoviesDetailsRepository.upsert(res)
                    res?.addedToFavourite = localFavouriteMoviesRepository.getFavouriteMovieById(movieId) != null
                    async { Pair(res, null) }
                } else async { Pair(localMovieDetails, response.getApiError()) }
            } catch (e: Exception) {
                if (e is HttpException) async { Pair(localMovieDetails, Error(e.code(), e.message())) }
                else async { Pair(localMovieDetails, Error(null, e.localizedMessage)) }
            }
        }
    }

    //region Code Abstractions
    private fun getMoviesBriefByOrder(orderBy: Int = 0, limit: Int = 20, page: Int? = null) = when(orderBy) {
        0 -> localMoviesRepository.getAllMoviesOrderedByDate(limit, limit * (page ?: 0))
        1 -> localMoviesRepository.getAllMoviesOrderedByRating(limit, limit * (page ?: 0))
        else -> localMoviesRepository.getAllMoviesOrderedByName(limit, limit * (page ?: 0))
    }

    private fun getFavouriteMoviesBriefByOrder(orderBy: Int = 0) = when(orderBy) {
        0 -> localFavouriteMoviesRepository.getAllFavouriteMoviesBriefOrderedByDate()
        1 -> localFavouriteMoviesRepository.getAllFavouriteMoviesBriefOrderedByRating()
        else -> localFavouriteMoviesRepository.getAllFavouriteMoviesBriefOrderedByName()
    }
    //endregion Code Abstractions
}