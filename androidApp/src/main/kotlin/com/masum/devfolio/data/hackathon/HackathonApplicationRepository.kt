package com.masum.devfolio.data.hackathon

import com.masum.devfolio.data.auth.AuthUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.util.concurrent.ConcurrentHashMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HackerDashboardResponse(
    @SerialName("status") val status: String? = null,
    @SerialName("rsvp_status") val rsvpStatus: String? = null,
    @SerialName("team") val team: HackerTeamInfo? = null
)

@Serializable
data class HackerTeamInfo(
    @SerialName("uuid") val uuid: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("code") val code: String? = null
)

enum class NativeApplicationStatus {
    NOT_APPLIED,
    APPLIED_UNDER_REVIEW,
    ACCEPTED_RSVP_PENDING,
    RSVP_CONFIRMED,
    WAITLISTED,
    WITHDRAWN,
    REJECTED
}

data class ApplicationStatusDetail(
    val status: NativeApplicationStatus,
    val teamName: String? = null,
    val teamCode: String? = null
)

class HackathonApplicationRepository(
    private val client: HttpClient = com.masum.devfolio.network.createDevfolioHttpClient()
) {
    companion object {
        private val statusCache = ConcurrentHashMap<String, ApplicationStatusDetail>()

        fun getCachedStatus(username: String, hackathonSlug: String): ApplicationStatusDetail? {
            return statusCache["$username:$hackathonSlug"]
        }

        fun updateCachedStatus(username: String, hackathonSlug: String, detail: ApplicationStatusDetail) {
            statusCache["$username:$hackathonSlug"] = detail
        }

        fun clearCache() {
            statusCache.clear()
        }
    }

    private val baseUrl = "https://api.devfolio.co/api"

    suspend fun getApplicationStatus(
        user: AuthUser,
        hackathonSlug: String
    ): Result<ApplicationStatusDetail> = runCatching {
        val cacheKey = "${user.username}:$hackathonSlug"
        val response: HackerDashboardResponse = client.get("$baseUrl/users/${user.username}/hackathon/$hackathonSlug") {
            header("Authorization", user.accessToken)
            header("access_token", user.accessToken)
            header("Cookie", user.rawCookies)
        }.body()

        val mappedStatus = when (response.status?.lowercase()?.trim()) {
            "submit", "submitted", "applied", "under_review" -> NativeApplicationStatus.APPLIED_UNDER_REVIEW
            "accept", "accepted" -> {
                if (response.rsvpStatus?.lowercase() == "attending" || response.rsvpStatus?.lowercase() == "confirmed") {
                    NativeApplicationStatus.RSVP_CONFIRMED
                } else {
                    NativeApplicationStatus.ACCEPTED_RSVP_PENDING
                }
            }
            "rsvp", "check_in" -> NativeApplicationStatus.RSVP_CONFIRMED
            "waitlist", "waitlisted" -> NativeApplicationStatus.WAITLISTED
            "withdraw", "withdrawn", "cancelled" -> NativeApplicationStatus.WITHDRAWN
            "reject_sent", "rejected" -> NativeApplicationStatus.REJECTED
            else -> NativeApplicationStatus.NOT_APPLIED
        }

        val detail = ApplicationStatusDetail(
            status = mappedStatus,
            teamName = response.team?.name,
            teamCode = response.team?.code
        )
        statusCache[cacheKey] = detail
        detail
    }

    suspend fun withdrawApplication(
        user: AuthUser,
        hackathonSlug: String
    ): Result<Boolean> = runCatching {
        client.post("$baseUrl/users/${user.username}/hackathon/$hackathonSlug/withdraw") {
            header("Authorization", user.accessToken)
            header("access_token", user.accessToken)
            header("Cookie", user.rawCookies)
            contentType(ContentType.Application.Json)
            setBody("{}")
        }
        statusCache["${user.username}:$hackathonSlug"] = ApplicationStatusDetail(NativeApplicationStatus.WITHDRAWN)
        true
    }

    suspend fun confirmRsvp(
        user: AuthUser,
        hackathonSlug: String
    ): Result<Boolean> = runCatching {
        client.post("$baseUrl/users/${user.username}/hackathon/$hackathonSlug/rsvp") {
            header("Authorization", user.accessToken)
            header("access_token", user.accessToken)
            header("Cookie", user.rawCookies)
            contentType(ContentType.Application.Json)
            setBody("{}")
        }
        true
    }
}
