package com.masum.devfolio.repository

import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.model.Project
import com.masum.devfolio.network.DevfolioApiService
import com.masum.devfolio.network.DevfolioApiServiceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DevfolioRepositoryImpl(
    private val apiService: DevfolioApiService = DevfolioApiServiceImpl()
) : DevfolioRepository {

    override fun getHackathonsStream(type: String, from: Int, size: Int): Flow<Result<List<Hackathon>>> = flow {
        emit(apiService.searchHackathons(type = type, from = from, size = size))
    }

    override suspend fun getHackathon(slug: String): Result<Hackathon> {
        return apiService.getHackathonDetail(slug)
    }

    override fun getProjectsStream(from: Int, size: Int): Flow<Result<List<Project>>> = flow {
        emit(apiService.searchProjects(from = from, size = size))
    }
}
