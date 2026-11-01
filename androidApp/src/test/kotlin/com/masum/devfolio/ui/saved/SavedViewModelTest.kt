package com.masum.devfolio.ui.saved

import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.repository.BookmarkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeBookmarkRepository : BookmarkRepository {
    private val _data = MutableStateFlow<Map<String, Hackathon>>(emptyMap())

    override fun getBookmarks(): Flow<List<Hackathon>> = _data.map { it.values.toList() }

    override fun isBookmarked(slug: String): Flow<Boolean> = _data.map { it.containsKey(slug) }

    override suspend fun toggleBookmark(hackathon: Hackathon) {
        val current = _data.value.toMutableMap()
        if (current.containsKey(hackathon.slug)) {
            current.remove(hackathon.slug)
        } else {
            current[hackathon.slug] = hackathon
        }
        _data.value = current
    }

    fun add(hackathon: Hackathon) {
        _data.value = _data.value + (hackathon.slug to hackathon)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class SavedViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private fun sampleHackathon(slug: String) = Hackathon(
        uuid = "uuid-$slug",
        name = "Hackathon $slug",
        slug = slug,
        tagline = "Tagline $slug",
        description = "Description $slug",
        coverImg = null,
        logoImg = null,
        startsAt = null,
        endsAt = null,
        regStartsAt = null,
        regEndsAt = null,
        isOnline = true,
        city = null,
        state = null,
        country = null,
        location = null,
        participantsCount = 10,
        primaryColor = null,
        siteUrl = null,
        discordUrl = null,
        faqs = emptyList(),
        participants = emptyList()
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialEmptyBookmarks() = runTest(testDispatcher) {
        val fakeRepo = FakeBookmarkRepository()
        val viewModel = SavedViewModel(fakeRepo)
        val job = backgroundScope.launch { viewModel.bookmarks.collect {} }
        advanceUntilIdle()

        assertTrue(viewModel.bookmarks.value.isEmpty())
        job.cancel()
    }

    @Test
    fun testRemoveBookmark() = runTest(testDispatcher) {
        val fakeRepo = FakeBookmarkRepository()
        val hack = sampleHackathon("devnexus")
        fakeRepo.add(hack)

        val viewModel = SavedViewModel(fakeRepo)
        val job = backgroundScope.launch { viewModel.bookmarks.collect {} }
        advanceUntilIdle()

        assertEquals(1, viewModel.bookmarks.value.size)
        assertEquals("devnexus", viewModel.bookmarks.value.first().slug)

        viewModel.removeBookmark(hack)
        advanceUntilIdle()

        assertTrue(viewModel.bookmarks.value.isEmpty())
        job.cancel()
    }
}
