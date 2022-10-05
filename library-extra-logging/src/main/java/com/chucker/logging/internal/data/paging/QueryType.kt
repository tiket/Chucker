package com.chucker.logging.internal.data.paging

sealed class QueryType {
    object Default : QueryType()
    data class Query(val query: String) : QueryType()
    data class QueryWithTag(val query: String, val tag: String) : QueryType()
}