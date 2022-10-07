package com.chucker.logging.internal.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.chucker.logging.internal.data.entity.LogData

@Dao
interface LoggingDao {
    @Query("SELECT id, tag, log, timeStamp FROM logging WHERE log LIKE :query AND tag = :tag ORDER BY timeStamp DESC")
    fun getFilteredWithTag(tag: String, query: String): List<LogData>

    @Query("SELECT id, tag, log, timeStamp FROM logging WHERE log LIKE :query ORDER BY timeStamp DESC")
    fun getFiltered(query: String): List<LogData>

    @Query("SELECT * FROM logging ORDER BY timeStamp DESC")
    fun getAllLiveData(): List<LogData>

    @Query("SELECT id, tag, log, timeStamp FROM logging WHERE log LIKE :query AND tag = :tag ORDER BY timeStamp DESC")
    fun getFilteredWithTagPaging(tag: String, query: String): PagingSource<Int, LogData>

    @Query("SELECT id, tag, log, timeStamp FROM logging WHERE log LIKE :query ORDER BY timeStamp DESC")
    fun getFilteredPaging(query: String): PagingSource<Int, LogData>

    @Query("SELECT * FROM logging ORDER BY timeStamp DESC")
    fun getAllLiveDataPaging(): PagingSource<Int, LogData>

    @Query("SELECT DISTINCT tag FROM logging")
    suspend fun getTags(): List<String>

    @Insert
    suspend fun insert(value: LogData): Long?

    @Query("DELETE FROM logging")
    suspend fun deleteAll()
}