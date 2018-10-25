package com.hamletleon.moviewratings.models.apiConfiguration

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Configurations",
        indices = [Index(value = ["value"], name = "Idx_Configuration_Value")])
class Configuration {
    @IConfigurationType @PrimaryKey var type: Int = ConfigurationType.NONE.id
    var httpBaseUrl: String = ""
    var httpsBaseUrl: String = ""
    var value: String = ""
        set(value) {
            valueType = value.substring(0, 1)
            size = if (value.toLowerCase().contains("original")) 0 else value.removeRange(0 , 1).toIntOrNull() ?: 0
            field = value
        }
    var valueType: String = ""
    var size: Int = 0
}