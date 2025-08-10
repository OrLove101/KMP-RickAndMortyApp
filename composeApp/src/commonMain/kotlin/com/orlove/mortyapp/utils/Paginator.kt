package com.orlove.mortyapp.utils

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}