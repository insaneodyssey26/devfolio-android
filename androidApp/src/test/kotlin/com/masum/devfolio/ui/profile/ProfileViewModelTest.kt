package com.masum.devfolio.ui.profile

import com.masum.devfolio.data.model.DevfolioUserProfile
import com.masum.devfolio.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

class FakeProfileRepository : ProfileRepository {
    var savedUsername: String? = null
    var shouldFail: Boolean = false
    var profileResult: DevfolioUserProfile? = null

    override suspend fun getSavedUsername(): String? = savedUsername

    override suspend fun saveUsername(username: String) {
        savedUsername = username
    }

    override suspend fun clearUsername() {
        savedUsername = null
    }

    override suspend fun getProfileByUsername(username: String): Result<DevfolioUserProfile> {
        if (shouldFail) return Result.failure(Exception("Network error"))
        val profile = profileResult ?: DevfolioUserProfile(
            uuid = "uuid-$username",
            username = username,
            firstName = "Test",
            lastName = "User",
            bio = "Builder bio"
        )
        return Result.success(profile)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitWithoutSavedUsernameIsIdle() = runTest(testDispatcher) {
        val fakeRepo = FakeProfileRepository()
        val viewModel = ProfileViewModel(fakeRepo)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is ProfileUiState.Idle)
        assertEquals("", viewModel.handleInput.value)
    }

    @Test
    fun testInitWithSavedUsernameLoadsProfile() = runTest(testDispatcher) {
        val fakeRepo = FakeProfileRepository().apply {
            savedUsername = "masum"
        }
        val viewModel = ProfileViewModel(fakeRepo)
        advanceUntilIdle()

        assertEquals("masum", viewModel.handleInput.value)
        val state = viewModel.uiState.value
        assertTrue(state is ProfileUiState.Success)
        assertEquals("masum", (state as ProfileUiState.Success).profile.username)
    }

    @Test
    fun testHandleChangeAndTrimmingOnLink() = runTest(testDispatcher) {
        val fakeRepo = FakeProfileRepository()
        val viewModel = ProfileViewModel(fakeRepo)
        advanceUntilIdle()

        viewModel.onHandleChange("@insane_odyssey")
        assertEquals("@insane_odyssey", viewModel.handleInput.value)

        viewModel.linkProfile()
        advanceUntilIdle()

        assertEquals("insane_odyssey", fakeRepo.savedUsername)
        val state = viewModel.uiState.value
        assertTrue(state is ProfileUiState.Success)
        assertEquals("insane_odyssey", (state as ProfileUiState.Success).profile.username)
    }

    @Test
    fun testNetworkErrorEmitsErrorState() = runTest(testDispatcher) {
        val fakeRepo = FakeProfileRepository().apply {
            shouldFail = true
        }
        val viewModel = ProfileViewModel(fakeRepo)
        advanceUntilIdle()

        viewModel.onHandleChange("error_user")
        viewModel.linkProfile()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is ProfileUiState.Error)
        assertEquals("Network error", (state as ProfileUiState.Error).message)
    }
}
