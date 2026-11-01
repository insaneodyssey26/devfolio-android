package com.masum.devfolio.repository

import com.masum.devfolio.model.Hackathon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

object BookmarkRepositoryImpl : BookmarkRepository {

    private val _bookmarks = MutableStateFlow<Map<String, Hackathon>>(emptyMap())

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
    }
}
