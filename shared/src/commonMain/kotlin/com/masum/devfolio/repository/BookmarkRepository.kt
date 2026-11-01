package com.masum.devfolio.repository

import com.masum.devfolio.model.Hackathon
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getBookmarks(): Flow<List<Hackathon>>
    fun isBookmarked(slug: String): Flow<Boolean>
    suspend fun toggleBookmark(hackathon: Hackathon)
}
