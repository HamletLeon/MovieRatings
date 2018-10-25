package com.hamletleon.moviewratings.repositories.local.configurations

import androidx.room.Dao
import androidx.room.Query
import com.hamletleon.moviewratings.models.apiConfiguration.Configuration
import com.hamletleon.moviewratings.repositories.local.core.BaseDAO

@Dao
abstract class ConfigurationsLocalRepository: BaseDAO<Configuration>() {
    @Query("SELECT * FROM Configurations WHERE type = :type")
    abstract fun getAllConfigurationsByType(type: Int): List<Configuration>

    @Query("SELECT * FROM Configurations WHERE type = :type AND size <= (:widthSize + 50) AND valueType LIKE :valueType ORDER BY size DESC LIMIT 1")
    abstract fun getCloseSizeConfiguration(type: Int, widthSize: Int, valueType: String = "w"): Configuration
}