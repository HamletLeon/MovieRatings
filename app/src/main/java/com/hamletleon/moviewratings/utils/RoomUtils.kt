package com.hamletleon.moviewratings.utils

import androidx.room.TypeConverter
import com.google.gson.Gson

class RoomUtils {
    @TypeConverter
    fun <T> stringToList(obj: String?, clazz: Class<Array<T>>): List<T> {
        if (obj == null) return emptyList()

        val jsonToList: Array<T> = Gson().fromJson(obj, clazz)
        return jsonToList.toList()
    }

    @TypeConverter
    fun <T> listToString(list: List<T>?): String {
        if (list == null) return ""
        return Gson().toJson(list)
    }
}