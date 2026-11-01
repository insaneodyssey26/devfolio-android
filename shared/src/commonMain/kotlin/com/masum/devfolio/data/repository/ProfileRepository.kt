package com.masum.devfolio.data.repository

import com.masum.devfolio.data.model.DevfolioUserProfile

interface ProfileRepository {
    suspend fun getProfileByUsername(username: String): Result<DevfolioUserProfile>
    suspend fun getSavedUsername(): String?
    suspend fun saveUsername(username: String)
    suspend fun clearUsername()
}
