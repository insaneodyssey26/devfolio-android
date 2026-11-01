package com.masum.devfolio.network

import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.model.Project

interface DevfolioApiService {
    suspend fun searchHackathons(
        type: String = "application_open",
        query: String? = null,
        from: Int = 0,
        size: Int = 20
    ): Result<List<Hackathon>>

    suspend fun getHackathonDetail(slug: String): Result<Hackathon>

    suspend fun searchProjects(
        query: String? = null,
        from: Int = 0,
        size: Int = 20
    ): Result<List<Project>>
}
