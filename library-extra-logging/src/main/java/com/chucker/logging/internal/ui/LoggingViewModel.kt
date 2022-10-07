package com.chucker.logging.internal.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.chucker.logging.internal.data.paging.QueryType
import com.chucker.logging.internal.data.repository.LoggingRepositoryProvider
import com.chucker.logging.internal.support.formatDate
import com.chucker.logging.internal.support.formatLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class LoggingViewModel : ViewModel() {

    private var currentFilter = ""
    private var currentSelectedTag = ""
    val allTagLiveData = MutableLiveData(emptyList<String>())

    val logDatas = MutableLiveData(emptyList<LogViewParam>())
    private var jobSearch: Job? = null

    private var currentFilterFlow = MutableStateFlow("")
    private var currentSelectedTagFlow = MutableStateFlow("")
    val pagingLogDatas by lazy {
        currentFilterFlow.combine(currentSelectedTagFlow) { f1, f2 -> f1 to f2 }.map {
            when {
                currentFilter.isBlank() && currentSelectedTag.isBlank() -> {
                    QueryType.Default
                }
                else -> {
                    if (currentSelectedTag.isBlank()) {
                        QueryType.Query(it.first)
                    } else {
                        QueryType.QueryWithTag(it.first, it.second)
                    }
                }
            }
        }.flatMapLatest { queryType ->
            LoggingRepositoryProvider.get().getPagerLog(queryType).flow.map { pagingData ->
                pagingData.map {
                    LogViewParam(
                        tag = it.tag,
                        logText = it.logString.formatLog(),
                        dateText = it.timeStamp.formatDate(),
                        queryText = currentFilter
                    )
                }
            }
        }.cachedIn(viewModelScope)
    }

    fun init() {
        doQuery()
        getAllTag()
    }

    fun updateItemsFilter(searchQuery: String) {
        currentFilter = searchQuery
        viewModelScope.launch(Dispatchers.IO) {
            currentFilterFlow.emit(searchQuery)
        }
    }

    private fun getAllTag() {
        viewModelScope.launch {
            allTagLiveData.value =
                withContext(Dispatchers.IO) { LoggingRepositoryProvider.get().getAllTag() }
        }
    }

    fun updateTag(selectedTag: String) {
        val tmpSelectedTag = if (selectedTag == "(empty)") {
            ""
        } else selectedTag
        currentSelectedTag = tmpSelectedTag
        viewModelScope.launch(Dispatchers.IO) {
            currentSelectedTagFlow.emit(selectedTag)
        }
    }

    fun clearLog() {
        viewModelScope.launch {
            LoggingRepositoryProvider.get().deleteAllLog()
            currentFilterFlow.emit(currentFilterFlow.value)
        }
    }

    fun doQuery() {
        jobSearch?.cancel()
        jobSearch = viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                with(LoggingRepositoryProvider.get()) {
                    when {
                        currentFilter.isBlank() && currentSelectedTag.isBlank() -> {
                            getAllLogLiveData()
                        }
                        else -> {
                            getFilteredLog(currentSelectedTag, currentFilter)
                        }
                    }
                }
            }.map {
                LogViewParam(
                    tag = it.tag,
                    logText = it.logString.formatLog(),
                    dateText = it.timeStamp.formatDate()
                )
            }

            logDatas.value = result
        }
    }
}