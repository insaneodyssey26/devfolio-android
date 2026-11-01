package com.masum.devfolio.repository

import com.masum.devfolio.model.Hackathon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object BookmarkRepositoryImpl : BookmarkRepository {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = true }
    private var storageDriver: BookmarkStorageDriver = InMemoryBookmarkStorageDriver()
    private val _bookmarks = MutableStateFlow<Map<String, Hackathon>>(emptyMap())

    fun initialize(driver: BookmarkStorageDriver) {
        this.storageDriver = driver
        val savedJson = driver.loadBookmarks()
        if (!savedJson.isNullOrBlank()) {
            runCatching {
                val list = json.decodeFromString<List<Hackathon>>(savedJson)
                _bookmarks.value = list.associateBy { it.slug }
            }
        }
    }

    override fun getBookmarks(): Flow<List<Hackathon>> = _bookmarks.asStateFlow().map { it.values.toList() }

    override fun isBookmarked(slug: String): Flow<Boolean> = _bookmarks.asStateFlow().map { it.containsKey(slug) }

    override suspend fun toggleBookmark(hackathon: Hackathon) {
        val current = _bookmarks.value.toMutableMap()
        if (current.containsKey(hackathon.slug)) {
            current.remove(hackathon.slug)
        } else {
            current[hackathon.slug] = hackathon
        }
        _bookmarks.value = current
        persist()
    }

    private fun persist() {
        runCatching {
            val list = _bookmarks.value.values.toList()
            val encoded = json.encodeToString(list)
            storageDriver.saveBookmarks(encoded)
        }
    }
}
