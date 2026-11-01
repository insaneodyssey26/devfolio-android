package com.masum.devfolio.repository

import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.model.Project
import kotlinx.coroutines.flow.Flow

interface DevfolioRepository {
    fun getHackathonsStream(type: String): Flow<Result<List<Hackathon>>>
    suspend fun getHackathon(slug: String): Result<Hackathon>
    fun getProjectsStream(): Flow<Result<List<Project>>>
}
