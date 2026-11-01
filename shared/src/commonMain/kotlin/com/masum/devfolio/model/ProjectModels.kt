package com.masum.devfolio.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectSearchRequest(
    @SerialName("web") val web: Boolean = true,
    @SerialName("query") val query: String? = null,
    @SerialName("from") val from: Int = 0,
    @SerialName("size") val size: Int = 20
)

@Serializable
data class ProjectSearchResponse(
    @SerialName("hits") val hits: ProjectSearchHits? = null
)

@Serializable
data class ProjectSearchHits(
    @SerialName("total") val total: TotalHits? = null,
    @SerialName("hits") val hits: List<ProjectHit> = emptyList()
)

@Serializable
data class ProjectHit(
    @SerialName("_source") val source: ProjectPayload? = null
)

@Serializable
data class ProjectPayload(
    @SerialName("uuid") val uuid: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("slug") val slug: String = "",
    @SerialName("tagline") val tagline: String? = null,
    @SerialName("links") val links: String? = null,
    @SerialName("video_url") val videoUrl: String? = null,
    @SerialName("pictures") val pictures: String? = null,
    @SerialName("has_github_link") val hasGithubLink: Boolean = false,
    @SerialName("hashtags") val hashtags: List<Hashtag> = emptyList(),
    @SerialName("members") val members: List<ProjectMember> = emptyList(),
    @SerialName("hackathon") val hackathon: ProjectHackathonPayload? = null
)

@Serializable
data class Hashtag(
    @SerialName("uuid") val uuid: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("verified") val verified: Boolean = false
)

@Serializable
data class ProjectMember(
    @SerialName("uuid") val uuid: String = "",
    @SerialName("username") val username: String = "",
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("profile_image") val profileImage: String? = null
)

@Serializable
data class ProjectHackathonPayload(
    @SerialName("uuid") val uuid: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("slug") val slug: String = "",
    @SerialName("logo") val logo: String? = null,
    @SerialName("primary_color") val primaryColor: String? = null
)

data class Project(
    val uuid: String,
    val name: String,
    val slug: String,
    val tagline: String,
    val githubLink: String?,
    val videoUrl: String?,
    val pictureUrls: List<String>,
    val hashtags: List<Hashtag>,
    val members: List<ProjectMember>,
    val hackathonName: String?,
    val hackathonLogo: String?
)

fun ProjectPayload.toDomain(): Project {
    val pictureList = pictures
        ?.split(",")
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
        ?.map { if (it.startsWith("http")) it else "https://assets.devfolio.co/$it" }
        .orEmpty()

    val githubUrl = links?.split(",")?.firstOrNull { it.contains("github.com") }?.trim()

    return Project(
        uuid = uuid,
        name = name,
        slug = slug,
        tagline = tagline.orEmpty(),
        githubLink = githubUrl,
        videoUrl = videoUrl,
        pictureUrls = pictureList,
        hashtags = hashtags,
        members = members,
        hackathonName = hackathon?.name,
        hackathonLogo = hackathon?.logo
    )
}
