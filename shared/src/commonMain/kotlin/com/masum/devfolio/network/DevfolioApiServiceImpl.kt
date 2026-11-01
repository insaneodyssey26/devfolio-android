package com.masum.devfolio.network

import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.model.HackathonDetailPayload
import com.masum.devfolio.model.HackathonSearchRequest
import com.masum.devfolio.model.HackathonSearchResponse
import com.masum.devfolio.model.Project
import com.masum.devfolio.model.ProjectSearchRequest
import com.masum.devfolio.model.ProjectSearchResponse
import com.masum.devfolio.model.toDomain
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DevfolioApiServiceImpl(
    private val client: HttpClient = createDevfolioHttpClient()
) : DevfolioApiService {

    private val baseUrl = "https://api.devfolio.co/api"

    override suspend fun searchHackathons(
        type: String,
        query: String?,
        from: Int,
        size: Int
    ): Result<List<Hackathon>> = runCatching {
        val requestBody = HackathonSearchRequest(
            type = type,
            web = true,
            query = query,
            from = from,
            size = size
        )
        val response: HackathonSearchResponse = client.post("$baseUrl/search/hackathons") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }.body()

        response.hits?.hits?.mapNotNull { it.source?.toDomain() }.orEmpty()
    }

    override suspend fun getHackathonDetail(slug: String): Result<Hackathon> = runCatching {
        val response: HackathonDetailPayload = client.get("$baseUrl/hackathons/$slug").body()
        response.toDomain()
    }

    override suspend fun searchProjects(
        query: String?,
        from: Int,
        size: Int
    ): Result<List<Project>> = runCatching {
        val requestBody = ProjectSearchRequest(
            web = true,
            query = query,
            from = from,
            size = size
        )
        val response: ProjectSearchResponse = client.post("$baseUrl/search/projects") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }.body()

        response.hits?.hits?.mapNotNull { it.source?.toDomain() }.orEmpty()
    }

    override suspend fun getProjectDetail(slug: String): Result<com.masum.devfolio.model.ProjectDetail> = runCatching {
        val response: com.masum.devfolio.model.ProjectDetailResponse = client.get("$baseUrl/projects/$slug").body()
        response.toDomain()
    }
}
