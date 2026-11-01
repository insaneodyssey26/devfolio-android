package com.masum.devfolio.repository

interface BookmarkStorageDriver {
    fun loadBookmarks(): String?
    fun saveBookmarks(json: String)
}

internal class InMemoryBookmarkStorageDriver : BookmarkStorageDriver {
    private var data: String? = null
    override fun loadBookmarks(): String? = data
    override fun saveBookmarks(json: String) {
        data = json
    }
}
