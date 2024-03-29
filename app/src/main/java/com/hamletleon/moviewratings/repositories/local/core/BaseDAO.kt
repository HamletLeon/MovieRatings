package com.hamletleon.moviewratings.repositories.local.core

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE

@Dao
abstract class BaseDAO<T> {
    @Insert(onConflict = IGNORE)
    abstract fun insert(obj: T): Long

    @Insert(onConflict = IGNORE)
    abstract fun insert(list: List<T>): List<Long>

    @Update
    abstract fun update(obj: T)

    @Update
    abstract fun update(obj: List<T>)

    @Delete
    abstract fun delete(obj: T)

    @Transaction
    open fun upsert(obj: T?){
        if (obj == null) return
        val id = insert(obj)
        if (id <= -1){
            update(obj)
        }
    }

    @Transaction
    open fun upsert(list: List<T>?){
        if(list == null) return
        val insertedResults: List<Long> = insert(list)
        val updateList: MutableList<T> = mutableListOf()

        for ((idx, obj) in insertedResults.withIndex()){
            if (obj <= -1){
                updateList.add(list[idx])
            }
        }
        if (updateList.any()){
            update(updateList)
        }
    }
}