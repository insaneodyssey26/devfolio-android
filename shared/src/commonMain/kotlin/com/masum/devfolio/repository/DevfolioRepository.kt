package com.masum.devfolio.repository

import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.model.Project
import kotlinx.coroutines.flow.Flow

interface DevfolioRepository {
    fun getHackathonsStream(type: String, from: Int = 0, size: Int = 20): Flow<Result<List<Hackathon>>>
    suspend fun getHackathon(slug: String): Result<Hackathon>
    fun getProjectsStream(from: Int = 0, size: Int = 25): Flow<Result<List<Project>>>
}
