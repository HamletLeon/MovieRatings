package com.hamletleon.moviewratings.repositories.local.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hamletleon.moviewratings.models.apiConfiguration.Configuration
import com.hamletleon.moviewratings.models.movies.FavouriteMovie
import com.hamletleon.moviewratings.models.movies.MovieDetails
import com.hamletleon.moviewratings.models.movies.MovieBrief
import com.hamletleon.moviewratings.repositories.local.configurations.ConfigurationsLocalRepository
import com.hamletleon.moviewratings.repositories.local.movies.FavouriteMoviesLocalRepository
import com.hamletleon.moviewratings.repositories.local.movies.MoviesBriefLocalRepository
import com.hamletleon.moviewratings.repositories.local.movies.MoviesDetailsLocalRepository

@Database(entities = [MovieBrief::class, MovieDetails::class, Configuration::class, FavouriteMovie::class], version = 2)
abstract class DatabaseContext: RoomDatabase() {
    abstract fun getMoviesBriefDAO(): MoviesBriefLocalRepository
    abstract fun getConfigurationDAO(): ConfigurationsLocalRepository
    abstract fun getMoviesDetailsDAO(): MoviesDetailsLocalRepository
    abstract fun getFavouriteMoviesDAO(): FavouriteMoviesLocalRepository

    companion object {
        private var INSTANCE: DatabaseContext? = null

        fun getInstance(context: Context): DatabaseContext =
                INSTANCE
                        ?: synchronized(DatabaseContext::class) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }

                }

        private fun buildDatabase(context: Context) = Room
                .databaseBuilder(context.applicationContext, DatabaseContext::class.java, "MovieRatings.Db")
                .fallbackToDestructiveMigration()
                .build()

        fun destroyInstance() {
            INSTANCE = null
        }

        fun clearAndResetAllTables(): Boolean {
            if (INSTANCE == null) return false

            // reset all auto-incrementalValues
            val query = SimpleSQLiteQuery("DELETE FROM sqlite_sequence")

            INSTANCE!!.beginTransaction()
            return try {
                INSTANCE!!.clearAllTables()
                INSTANCE!!.query(query)
                INSTANCE!!.setTransactionSuccessful()
                true
            } catch (e: Exception){
                false
            } finally {
                INSTANCE!!.endTransaction()
            }
        }
    }
}