package com.orlove.mortyapp.utils

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    private val getNextKey: suspend (List<Item>) -> Key,
    private val onError: suspend (Throwable?) -> Unit,
    private val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
) : Paginator<Key, Item> {
    private var currentKey = initialKey
    private var isMakingRequest = false
    override suspend fun loadNextItems() {
        if (isMakingRequest) return
        isMakingRequest = true
        onLoadUpdated(true)
        val result = try {
            onRequest(currentKey)
        } catch (t: Throwable) {
            Result.failure(t)
        }
        isMakingRequest = false
        val items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentKey = getNextKey(items)
        onSuccess(items, currentKey)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}