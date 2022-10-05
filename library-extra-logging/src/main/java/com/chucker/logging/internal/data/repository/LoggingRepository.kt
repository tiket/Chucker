package com.chucker.logging.internal.data.repository

import androidx.paging.Pager
import com.chucker.logging.internal.data.entity.LogData
import com.chucker.logging.internal.data.paging.QueryType

internal interface LoggingRepository {

    suspend fun insertLog(logData: LogData)

    suspend fun deleteAllLog()

    suspend fun getFilteredLog(tag: String, query: String): List<LogData>

    suspend fun getAllLogLiveData(): List<LogData>

    suspend fun getAllTag(): List<String>

    fun getPagerLog(queryType: QueryType): Pager<Int, LogData>
}