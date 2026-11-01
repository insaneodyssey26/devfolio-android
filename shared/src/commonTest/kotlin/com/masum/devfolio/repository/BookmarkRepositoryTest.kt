package com.masum.devfolio.repository

import com.masum.devfolio.model.Hackathon
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BookmarkRepositoryTest {

    private fun sampleHackathon(slug: String) = Hackathon(
        uuid = "uuid-$slug",
        name = "Hackathon $slug",
        slug = slug,
        tagline = "Tagline $slug",
        description = "Description $slug",
        coverImg = null,
        logoImg = null,
        startsAt = "2026-11-20T10:00:00Z",
        endsAt = "2026-11-22T18:00:00Z",
        regStartsAt = null,
        regEndsAt = null,
        isOnline = true,
        city = "Bengaluru",
        state = "KA",
        country = "India",
        location = "Bengaluru, KA, India",
        participantsCount = 100,
        primaryColor = "#3770FF",
        siteUrl = "https://$slug.devfolio.co",
        discordUrl = null,
        faqs = emptyList(),
        participants = emptyList()
    )

    @Test
    fun testInitialBookmarksEmpty() = runTest {
        val repo = BookmarkRepositoryImpl
        val bookmarks = repo.getBookmarks().first()
        for (b in bookmarks) {
            repo.toggleBookmark(b)
        }
        val emptyBookmarks = repo.getBookmarks().first()
        assertTrue(emptyBookmarks.isEmpty())
    }

    @Test
    fun testBookmarkPersistenceWithStorageDriver() = runTest {
        val testDriver = InMemoryBookmarkStorageDriver()
        BookmarkRepositoryImpl.initialize(testDriver)

        val hackathon = sampleHackathon("persist-test")
        BookmarkRepositoryImpl.toggleBookmark(hackathon)

        assertTrue(testDriver.loadBookmarks()?.contains("persist-test") == true)

        // Simulate new app launch with same driver
        BookmarkRepositoryImpl.initialize(testDriver)
        val loaded = BookmarkRepositoryImpl.getBookmarks().first()
        assertEquals(1, loaded.size)
        assertEquals("persist-test", loaded.first().slug)

        // Clean up
        BookmarkRepositoryImpl.toggleBookmark(hackathon)
    }

    @Test
    fun testToggleBookmarkAddsAndRemoves() = runTest {
        val repo = BookmarkRepositoryImpl
        val hack = sampleHackathon("hack-1")

        assertFalse(repo.isBookmarked("hack-1").first())

        repo.toggleBookmark(hack)
        assertTrue(repo.isBookmarked("hack-1").first())
        val list = repo.getBookmarks().first()
        assertEquals(1, list.size)
        assertEquals("hack-1", list.first().slug)

        repo.toggleBookmark(hack)
        assertFalse(repo.isBookmarked("hack-1").first())
        assertTrue(repo.getBookmarks().first().isEmpty())
    }
}
